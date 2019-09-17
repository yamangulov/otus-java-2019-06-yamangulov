package ru.otus;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class JsonDIYTest {
    @Test
    void toJson() {
        Gson gson = new Gson();
        JsonDIY json = new JsonDIY();

        assertEquals(gson.toJson(null), json.toJson(null));
        assertEquals(gson.toJson((byte)1), json.toJson((byte)1));
        assertEquals(gson.toJson((byte)279), json.toJson((byte)279));
        assertEquals(gson.toJson((short) 4), json.toJson((short) 4));
        assertEquals(gson.toJson(8), json.toJson(8));
        assertEquals(gson.toJson(88L), json.toJson(88L));
        assertEquals(gson.toJson(88f), json.toJson(88f));
        assertEquals(gson.toJson(88d), json.toJson(88d));
        assertEquals(gson.toJson('d'), json.toJson('d'));
        assertEquals(gson.toJson("Simple string"), json.toJson("Simple string"));
        assertEquals(gson.toJson(new int[]{8, 88, 888}), json.toJson(new int[]{8, 88, 888}));
        assertEquals(gson.toJson(new String[]{"string 1", "string 2", "string 3"}), json.toJson(new String[]{"string 1", "string 2", "string 3"}));

        List<String> list = new ArrayList<>();
        list.add("ls1");
        list.add("ls2");
        list.add("ls3");
        list.add("ls4");
        assertEquals(gson.toJson(list), json.toJson(list));

        List list2 = Collections.singletonList(list);
        assertEquals(gson.toJson(list2), json.toJson(list2));

        assertEquals(gson.toJson(Collections.singletonList(111)), json.toJson(Collections.singletonList(111)));

        Map<Integer, String> map = Collections.singletonMap(1, "one");
        assertEquals(gson.toJson(map), json.toJson(map));

        Person person = new Person();
        Admin admin = new Admin();
        assertEquals(gson.toJson(person), json.toJson(person));
        assertEquals(gson.toJson(admin), json.toJson(admin));

    }
}
