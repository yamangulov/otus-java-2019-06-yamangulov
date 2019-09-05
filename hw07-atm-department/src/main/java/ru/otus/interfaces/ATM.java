package ru.otus.interfaces;

import ru.otus.classes.Cassette;

import java.util.Map;

public interface ATM {

    void loadCassette(Cassette cassette);

    void loadSetCassettes();

    long getBalance();

    boolean accept (Map<Banknotes, Integer> banknotes);

    Map<Banknotes, Integer> dispense(long requiredSum);

    Map<Banknotes, Integer> dispenseRest();
}
