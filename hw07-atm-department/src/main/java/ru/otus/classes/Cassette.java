package ru.otus.classes;

public class Cassette {

    private int amount;
    private long value;

    private Cassette() {}

    public Cassette(long value, int amount) {
        this.amount = amount;
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public int getAmount() {
        return amount;
    }

}
