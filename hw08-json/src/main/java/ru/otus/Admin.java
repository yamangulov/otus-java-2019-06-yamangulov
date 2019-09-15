package ru.otus;

import java.util.*;

public class Admin extends Person {
    private int i = 111;
    private long l = 222L;
    private boolean b = false;
    private char c = 'Z';
    private Integer integer = 333;
    private String string = "The super user";
    private double[] array = {4.4444, 5.5555, 6.6666};
    private Set<String> set = new HashSet<>();
    private Map<Integer, String> map = new HashMap<>();
    private List<Integer> list = new ArrayList<>();
    private double d = 777d;
    private float f = 888f;
    private short sh = (short)35;
    private byte bb = (byte) 299;

    public Admin() {
        super("Admin", 50);

        set.add("Right 1");
        set.add("Right 2");
        set.add("Right 3");
        set.add("Right 4");

        list.add(1);
        list.add(2);
        list.add(10);
        list.add(20);

        map.put(1, "Directory 1");
        map.put(2, "Directory 2");
        map.put(3, "Directory 3");
        map.put(4, "Directory 4");
    }
}
