package ru.otus;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

public class HelloOtus {

    private static Range<Integer> beginnerScool = Range.closed (1, 4);
    private static Range<Integer> middleScool = Range.closed (5, 9);
    private static Range<Integer> topMiddleScool = Range.closed (10, 11);

    private static RangeMap<Integer, String> schoolsMap = TreeRangeMap.create();

    public Range<Integer> getBeginnerScool() {
        return beginnerScool;
    }

    public Range<Integer> getMiddleScool() {
        return middleScool;
    }

    public Range<Integer> getTopMiddleScool() {
        return topMiddleScool;
    }

    public RangeMap<Integer, String> getSchoolsMap() {
        return schoolsMap;
    }

    public static void main(String[] args) {
        schoolsMap.put(beginnerScool, "Начальная школа - с 1 по 4 класс");
        schoolsMap.put(middleScool, "Средняя школа - с 5 по 9 класс");
        schoolsMap.put(topMiddleScool, "Старшие классы - 10-11 классы");

        System.out.println(schoolsMap.getEntry(1));
        System.out.println(schoolsMap.toString());
    }

}