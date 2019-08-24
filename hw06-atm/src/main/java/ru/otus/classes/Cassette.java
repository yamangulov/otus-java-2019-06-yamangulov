package ru.otus.classes;

import ru.otus.enums.BanknotesRU;

public class Cassette {

    private long sum;
    private int amount;
    private long value = BanknotesRU.HUNDRED.getValue();

    private Cassette() {}

    public Cassette(long value, int amount) {
        this.sum = amount * value;
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
