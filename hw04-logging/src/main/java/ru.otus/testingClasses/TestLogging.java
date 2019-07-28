package ru.otus.testingClasses;

import ru.otus.logging.Log;

public class TestLogging {
    @Log
    public void calculation(int param) {
        System.out.println("calculation has run!");
    }
}
