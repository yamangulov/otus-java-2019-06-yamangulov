package ru.otus.interfaces;

import ru.otus.classes.Cassette;

import java.util.Map;

public interface IATM {

    void loadCassette(Cassette cassette);

    void loadSetCassettes();

    long getBalance();

    boolean accept (Map<IBanknotes, Integer> banknotes);

    Map<IBanknotes, Integer> dispense(long requiredSum);

    Map<IBanknotes, Integer> dispenseRest();
}
