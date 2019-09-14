package ru.otus;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Start {

    private static Logger logger = LoggerFactory.getLogger(Start.class);

    public static void main(String[] args) {
        Gson gson = new Gson();
        JsonDIY json = new JsonDIY();
        System.out.println(gson.toJson(null));
        System.out.println(json.toJson(null));
        System.out.println(gson.toJson((byte) 1));
        System.out.println(json.toJson((byte) 1));
        System.out.println(gson.toJson((byte) 279));
        System.out.println(json.toJson((byte) 279));
        System.out.println(gson.toJson((short) 4));
        System.out.println(json.toJson((short) 4));
        System.out.println(gson.toJson(8));
        System.out.println(json.toJson(8));
        System.out.println(gson.toJson(88L));
        System.out.println(json.toJson(88L));
        System.out.println(gson.toJson(88f));
        System.out.println(json.toJson(88f));
        System.out.println(gson.toJson(88d));
        System.out.println(json.toJson(88d));
        System.out.println(gson.toJson('d'));
        System.out.println(json.toJson('d'));
        System.out.println(gson.toJson("Simple string"));
        System.out.println(json.toJson("Simple string"));
        System.out.println(gson.toJson(new int[]{8, 88, 888}));
        System.out.println(json.toJson(new int[]{8, 88, 888}));
        System.out.println(gson.toJson(new String[]{"string 1", "string 2", "string 3"}));
        System.out.println(json.toJson(new String[]{"string 1", "string 2", "string 3"}));
        List<String> list = new ArrayList<>();
        list.add("ls1");
        list.add("ls2");
        list.add("ls3");
        list.add("ls4");
        System.out.println(gson.toJson(list));
        System.out.println(json.toJson(list));
        List list2 = Collections.singletonList(list);
        System.out.println(gson.toJson(list2));
        System.out.println(json.toJson(list2));
        System.out.println(gson.toJson(Collections.singletonList(111)));
        System.out.println(json.toJson(Collections.singletonList(111)));
        Map<Integer, String> map = Collections.singletonMap(1, "one");
        System.out.println(gson.toJson(map));
        System.out.println(json.toJson(map));
    }
}
