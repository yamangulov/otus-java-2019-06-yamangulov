package ru.otus.classes;

import ru.otus.interfaces.IStateMemory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StateMemory implements IStateMemory {

    private Set<ATM> atms = new HashSet<>(); //текущее состояние
    private Set<ATM> initAtms = new HashSet<>(); //сохраненное состояние

    private Map<ATM, Map<Long, Integer>> atmCassetes = new HashMap<>(); //текущее состояние
    private Map<ATM, Map<Long, Integer>> initAtmCassetes = new HashMap<>(); //сохраненное состояние

    public Set<ATM> getInitAtms() {
        return initAtms;
    }

    public Map<ATM, Map<Long, Integer>> getInitAtmCassetes() {
        return initAtmCassetes;
    }

    public void setInitAtms(Set<ATM> initAtms) {
        this.initAtms = initAtms;
    }

    public void setInitAtmCassetes(Map<ATM, Map<Long, Integer>> initAtmCassetes) {
        this.initAtmCassetes = initAtmCassetes;
    }

    public Set<ATM> getAtms() {
        return atms;
    }

    public Map<ATM, Map<Long, Integer>> getAtmCassetes() {
        return atmCassetes;
    }

    public void setAtms(Set<ATM> atms) {
        this.atms = atms;
    }

    public void setAtmCassetes(Map<ATM, Map<Long, Integer>> atmCassetes) {
        this.atmCassetes = atmCassetes;
    }

    public void rememberATM(ATM atm) {
        atms.add(atm);
        atmCassetes.put(atm, new HashMap<>());
    }

    public void rememberCassetteToATM(Cassette cassette, ATM atm) {
        Map<Long, Integer> cassettes = atmCassetes.get(atm);
        cassettes.put(cassette.getValue(), cassette.getAmount());
        atmCassetes.replace(atm, cassettes);
    }

}
