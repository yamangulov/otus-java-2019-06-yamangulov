package ru.otus.utils;

import java.util.ArrayList;
import java.util.List;

public class Benchmark implements BenchmarkMBean {

    private List<Integer> list = new ArrayList<>();
    private final int loopCounter;

    public Benchmark(int loopCounter) {
        this.loopCounter = loopCounter;
    }

    public void runForOutOfMemory() throws InterruptedException {

        for (int i = 1; i < loopCounter; ++i) {

            list.add(Integer.valueOf(i));

            if (i % 10_000 == 0) {
                for (int j = 0; j < this.getSize() / 2; ++j) {
                    list.set(j, null);
                }
            }

            if (i % 30_000 == 0) {
                Thread.sleep(300);
            }
        }
    }

    @Override
    public int getSize() {
        return list.size();
    }
}
