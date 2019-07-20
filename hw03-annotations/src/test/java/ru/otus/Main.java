package ru.otus;

import ru.otus.runner.Runner;

public class Main {
    public static void main(String[] args) {
        Runner runner = new Runner();
        runner.run(AnnotationsClass.class);
    }
}
