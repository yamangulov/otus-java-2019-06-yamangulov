package ru.otus.interfaces.memento;

import ru.otus.classes.ATMImpl;

import java.util.Map;
import java.util.Set;

public interface Memento {
    Set<ATMImpl> getAtmsState();
    Map<ATMImpl, Map<Long, Integer>> getAtmCassettesState();
}
