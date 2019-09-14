package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.*;
import java.util.*;
import java.lang.reflect.*;

public class JsonDIY {

    private static Logger logger = LoggerFactory.getLogger(JsonDIY.class);

    public String toJson(Object obj) {
        return obj == null ? JsonObject.NULL.toString() : this.toJsonValue(obj).toString();
    }

    private JsonValue toJsonValue(Object object) {

        if (object == null) { return JsonValue.NULL; }

        if (object instanceof Byte) {
            return Json.createValue((Byte)object);
        }
        else if (object instanceof Short) {
            return Json.createValue((Short)object);
        }
        else if (object instanceof Integer) {
            return Json.createValue((Integer)object);
        }
        else if (object instanceof Long) {
            return Json.createValue((Long)object);
        }
        else if (object instanceof Float) {
            return Json.createValue((Float)object);
        }
        else if (object instanceof Double) {
            return Json.createValue((Double)object);
        }
        else if (object instanceof Boolean) {
            Boolean value = (Boolean)object;
            return value ? JsonValue.TRUE : JsonValue.FALSE;
        }
        else if (object instanceof String || object instanceof Character) {
            return Json.createValue(object.toString());
        }
        else if (object instanceof Collection) {
            return collectionToJsonBuilder((Collection)object).build();
        }
        else if (object instanceof Map) {
            return mapToJsonBuilder((Map)object).build();
        }
        else if (object.getClass().isArray()) {
            return arrayToJsonBuilder(object).build();
        }
        else {
            return objectToJsonValue(object);
        }
    }

    private JsonArrayBuilder collectionToJsonBuilder(Collection object) {
        Object[] arrObjects = object.toArray();
        return arrayToJsonBuilder(arrObjects);
    }

    private JsonArrayBuilder arrayToJsonBuilder(Object arrObjects) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (int i = 0; i < Array.getLength(arrObjects); i++) {
            Object arrayElement = Array.get(arrObjects, i);
            arrayBuilder.add(toJsonValue(arrayElement));
        }
        return arrayBuilder;
    }

    private JsonObjectBuilder mapToJsonBuilder(Map object) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        object.forEach((key, val) -> jsonObjectBuilder.add(key.toString(), toJsonValue(val)));

        return jsonObjectBuilder;
    }


    private JsonValue objectToJsonValue(Object obj) {

        JsonObjectBuilder builder = Json.createObjectBuilder();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {

            boolean isAccessible = field.canAccess(obj);
            if (!isAccessible) { field.setAccessible(true); }

            try {
                JsonValue jsonValue = toJsonValue(field.get(obj));
                builder.add(field.getName(), jsonValue);
            }
            catch (Exception e) {
                logger.error("Error in method JsonDIY.JsonValue(): ", e);
            }

            if (!isAccessible) { field.setAccessible(false); }
        }

        return builder.build();
    }

}
