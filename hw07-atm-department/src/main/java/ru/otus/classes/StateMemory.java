package ru.otus.classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StateMemory {

    private Set<ATMImpl> atms = new HashSet<>(); //текущее состояние
    private Set<ATMImpl> initAtms = new HashSet<>(); //сохраненное состояние

    private Map<ATMImpl, Map<Long, Integer>> atmCassetes = new HashMap<>(); //текущее состояние
    private Map<ATMImpl, Map<Long, Integer>> initAtmCassetes = new HashMap<>(); //сохраненное состояние

    public Set<ATMImpl> getInitAtms() {
        return initAtms;
    }

    public Map<ATMImpl, Map<Long, Integer>> getInitAtmCassetes() {
        return initAtmCassetes;
    }

    public void setInitAtms(Set<ATMImpl> initAtms) {
        this.initAtms = initAtms;
    }

    public void setInitAtmCassetes(Map<ATMImpl, Map<Long, Integer>> initAtmCassetes) {
        this.initAtmCassetes = initAtmCassetes;
    }

    public Set<ATMImpl> getAtms() {
        return atms;
    }

    public Map<ATMImpl, Map<Long, Integer>> getAtmCassetes() {
        return atmCassetes;
    }

    public void setAtms(Set<ATMImpl> atms) {
        this.atms = atms;
    }

    public void setAtmCassetes(Map<ATMImpl, Map<Long, Integer>> atmCassetes) {
        this.atmCassetes = atmCassetes;
    }

    public void rememberATM(ATMImpl atm) {
        atms.add(atm);
        atmCassetes.put(atm, new HashMap<>());
    }

    public void rememberCassetteToATM(Cassette cassette, ATMImpl atm) {
        Map<Long, Integer> cassettes = atmCassetes.get(atm);
        cassettes.put(cassette.getValue(), cassette.getAmount());
        atmCassetes.replace(atm, cassettes);
    }

}
