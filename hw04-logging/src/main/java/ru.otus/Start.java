package ru.otus;

import ru.otus.testingClasses.TestLogging;

public class Start {
    public static void main(String[] args) {
        TestLogging testLogging = new TestLogging();
        testLogging.calculation(555);

        TestLogging testLogging2 = new TestLogging();
        testLogging2.calculationString("666");
    }
}
