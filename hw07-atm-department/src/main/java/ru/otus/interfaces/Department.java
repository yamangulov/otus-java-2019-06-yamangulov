package ru.otus.interfaces;

import ru.otus.classes.ATMImpl;

import java.util.Map;

public interface Department {

    ATMImpl addATM();

    ATMImpl addATM(ATMImpl atm);

    Map<Banknotes, Integer> dispenseTotalRest();

    void saveState();

    void saveModifiedState();

    void restoreState();
}
