package ru.otus.interfaces;

import ru.otus.classes.ATM;

import java.util.Map;

public interface IDepartment {

    ATM addATM();

    ATM addATM(ATM atm);

    Map<IBanknotes, Integer> dispenseTotalRest();

    void rememberState();

    void undoState();
}
