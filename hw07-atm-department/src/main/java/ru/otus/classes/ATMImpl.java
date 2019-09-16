package ru.otus.classes;

import ru.otus.classes.memento.Originator;
import ru.otus.enums.BanknotesRU;
import ru.otus.interfaces.ATM;
import ru.otus.interfaces.Banknotes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATMImpl implements ATM {

    private DispenserImpl dispenser = new DispenserImpl();

    private Originator departmentState;

    public DispenserImpl getDispenser() {
        return dispenser;
    }

    public ATMImpl() {
    }

    public ATMImpl(Originator departmentState) {
        this.departmentState = departmentState;
    }

    @Override
    public void loadCassette(Cassette cassette) {
        dispenser.getStoredBanknotes().put(cassette.getValue(), cassette.getAmount());
        if (departmentState != null) {
            departmentState.rememberCassetteToATM(cassette, this);
        }
    }

    @Override
    public void loadSetCassettes() {
        List<Cassette> cassettes = new ArrayList<Cassette>();
        cassettes.add(new Cassette(BanknotesRU.FIFTY.getValue(), 1000));
        cassettes.add(new Cassette(BanknotesRU.HUNDRED.getValue(), 1000));
        cassettes.add(new Cassette(BanknotesRU.FIVE_HUNDRED.getValue(), 1000));
        cassettes.add(new Cassette(BanknotesRU.THOUSAND.getValue(), 1000));
        cassettes.add(new Cassette(BanknotesRU.FIVE_THOUSEND.getValue(), 1000));
        for (Cassette cassette: cassettes) {
            dispenser.getStoredBanknotes().put(cassette.getValue(), cassette.getAmount());
            if (departmentState != null) {
                departmentState.rememberCassetteToATM(cassette, this);
            }
        }
    }

    //примеры применения Adapter - банкомат принимает enum банкнот, но диспенсер оперирует уже long, чтобы мог работать TreeMap
    //потому что ключ типа long поддерживает сортировку по нему, а ключ типа enum - было бы труднее сделать что-то для этого

    @Override
    public long getBalance() {
        return dispenser.getBalance();
    }

    @Override
    public boolean accept(Map<Banknotes, Integer> banknotes) {
        Map<Long, Integer> banknotesToValue = new HashMap<>();
        banknotes.forEach((banknote, amount) -> {
            banknotesToValue.put(banknote.getValue(), amount);
        });
        return dispenser.accept(banknotesToValue);
    }

    @Override
    public Map<Banknotes, Integer> dispense(long requiredSum) {
        Map<Banknotes, Integer> mapOfBanknotes = new HashMap<>();
        Map<Long, Integer> mapOfBanknotesPerValue = dispenser.dispense(requiredSum);
        mapOfBanknotesPerValue.forEach((banknoteValue, amount) -> {
            mapOfBanknotes.put(BanknotesRU.getBanknote(banknoteValue), amount);
        });
        return mapOfBanknotes;
    }

    @Override
    public Map<Banknotes, Integer> dispenseRest() {
        Map<Banknotes, Integer> mapOfBanknotes = new HashMap<>();
        Map<Long, Integer> mapOfBanknotesPerValue = dispenser.dispenseRest();
        mapOfBanknotesPerValue.forEach((banknoteValue, amount) -> {
            mapOfBanknotes.put(BanknotesRU.getBanknote(banknoteValue), amount);
        });
        return mapOfBanknotes;
    }

}
