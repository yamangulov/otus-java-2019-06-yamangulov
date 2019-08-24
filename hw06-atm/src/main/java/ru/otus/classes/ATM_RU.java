package ru.otus.classes;

import ru.otus.enums.BanknotesRU;
import ru.otus.interfaces.IATM;
import ru.otus.interfaces.IBanknotes;

import java.util.*;

public class ATM_RU implements IATM {

    private Dispenser dispenser = new Dispenser();

    public ATM_RU() {
    }

    @Override
    public void loadCassette(Cassette cassette) {
        dispenser.storedBanknotes.put(cassette.getValue(), cassette.getAmount());
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
            dispenser.storedBanknotes.put(cassette.getValue(), cassette.getAmount());
        }
    }

    @Override
    public long getBalance() {
        long balance = 0;
        for (Map.Entry<Long, Integer> entry: dispenser.storedBanknotes.entrySet()) {
            balance += entry.getKey() * entry.getValue();
        }
        return balance;
    }

    //примеры применения Adapter - банкомат принимает enum банкнот, но диспенсер оперирует уже long, чтобы мог работать TreeMap
    //потому что ключ типа long поддерживает сортировку по нему, а ключ типа enum - было бы труднее сделать что-то для этого

    @Override
    public boolean accept(Map<IBanknotes, Integer> banknotes) {
        Map<Long, Integer> banknotesToValue = new HashMap<>();
        banknotes.forEach((banknote, amount) -> {
            banknotesToValue.put(banknote.getValue(), amount);
        });
        return dispenser.accept(banknotesToValue);
    }

    @Override
    public Map<IBanknotes, Integer> dispense(long requiredSum) {
        Map<IBanknotes, Integer> mapOfBanknotes = new HashMap<>();
        Map<Long, Integer> mapOfBanknotesPerValue = dispenser.dispense(requiredSum);
        mapOfBanknotesPerValue.forEach((banknoteValue, amount) -> {
            mapOfBanknotes.put(BanknotesRU.getBanknote(banknoteValue), amount);
        });
        return mapOfBanknotes;
    }

    @Override
    public Map<IBanknotes, Integer> dispenseRest() {
        Map<IBanknotes, Integer> mapOfBanknotes = new HashMap<>();
        Map<Long, Integer> mapOfBanknotesPerValue = dispenser.dispenseRest();
        mapOfBanknotesPerValue.forEach((banknoteValue, amount) -> {
            mapOfBanknotes.put(BanknotesRU.getBanknote(banknoteValue), amount);
        });
        return mapOfBanknotes;
    }

}
