package ru.otus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Integer> list1 = new DIYarrayList<>();
        list1.add(0);
        System.out.println(list1.get(0));

        List<String> list2 = new DIYarrayList<>();
        list2.add("Hellow!");
        System.out.println(list2.get(0));

        List<Integer> list3 = new DIYarrayList<>();
        Collection<Integer> coll = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            coll.add(i);
        }


        System.out.println(coll);

        coll.clear();

        for (int i = 0; i < 30; i++) {
            int el = (int) (Math.random() * 100);
            coll.add(el);
        }

        System.out.println(coll);

        list3.addAll(coll);

        for (int element: list3) {
            System.out.println(element);
        }

        Collections.sort(list3);

        for (int element: list3) {
            System.out.println(element);
        }

        Collections.sort(list3, Collections.reverseOrder());

        for (int element: list3) {
            System.out.println(element);
        }

    }
}
