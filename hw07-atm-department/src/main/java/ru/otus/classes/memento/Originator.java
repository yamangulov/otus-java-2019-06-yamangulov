package ru.otus.classes.memento;

import ru.otus.classes.ATMImpl;
import ru.otus.classes.Cassette;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Originator {


    private Set<ATMImpl> atmsState = new HashSet<>(); //текущее состояние atm
    private Map<ATMImpl, Map<Long, Integer>> atmCassettesState = new HashMap<>(); //текущее состояние всех кассет в каждом atm


    public Set<ATMImpl> getAtmsState() {
        return atmsState;
    }
    public Map<ATMImpl, Map<Long, Integer>> getAtmCassettesState() {
        return atmCassettesState;
    }


    public Memento saveState() {
        return new Memento(atmsState, atmCassettesState);
    }

    public Memento saveModifiedState(Memento memento) {
        memento.setAtmsState(atmsState);
        memento.setAtmCassettesState(atmCassettesState);
        return memento;
    }

    public void restoreState(Memento memento) {
        this.atmsState = memento.getAtmsState();
        this.atmCassettesState = memento.getAtmCassettesState();
    }

    public void rememberATM(ATMImpl atm) {
        atmsState.add(atm);
        atmCassettesState.put(atm, new HashMap<>());
    }

    public void rememberCassetteToATM(Cassette cassette, ATMImpl atm) {
        Map<Long, Integer> cassettes = atmCassettesState.get(atm);
        cassettes.put(cassette.getValue(), cassette.getAmount());
        atmCassettesState.replace(atm, cassettes);
    }

}
