package ru.otus.classes;

import ru.otus.exceptions.NotEnoughMoneyException;
import ru.otus.exceptions.NotSupportedBanknotesSet;
import ru.otus.interfaces.Dispenser;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class DispenserImpl implements Dispenser {

    private Map<Long, Integer> storedBanknotes = new TreeMap<>(Collections.reverseOrder());

    public Map<Long, Integer> getStoredBanknotes() {
        return storedBanknotes;
    }

    @Override
    public boolean accept(Map<Long, Integer> banknotes) {
        try {
            banknotes.forEach(((banknote, amount) -> {
                int storedBanknoteAmount;
                if (storedBanknotes.get(banknote) != null) {
                    storedBanknoteAmount = storedBanknotes.get(banknote);
                } else {
                    storedBanknoteAmount = 0;
                }
                storedBanknotes.put(banknote, storedBanknoteAmount + amount);
                System.out.println("Принято банкнот номиналом " + banknote + " рублей: " + amount + " шт." );
            }));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }



    @Override
    public Map<Long, Integer> dispense(long requiredSum) {
        Map<Long, Integer> map = new TreeMap<>(Collections.reverseOrder());
        Map<Long, Integer> newStoredBanknotes = new TreeMap<>(Collections.reverseOrder());

        try {
            if (this.getBalance() < requiredSum) {
                throw new NotEnoughMoneyException();
            }
            long sum = requiredSum;
            long acceptedSum = 0;
            for (Map.Entry<Long, Integer> entry : storedBanknotes.entrySet()) {
                Long banknote = entry.getKey();
                Integer amount = entry.getValue();
                int numberOfBanknotes = (int) (sum / banknote);
                while (numberOfBanknotes > amount) {
                    numberOfBanknotes--;
                }
                sum = sum - numberOfBanknotes * banknote;
                acceptedSum += numberOfBanknotes * banknote;
                if (numberOfBanknotes > 0) {
                    map.put(banknote, numberOfBanknotes);
                    newStoredBanknotes.put(banknote, storedBanknotes.get(banknote) - numberOfBanknotes);
                    System.out.println("Может быть выдано банкнот номиналом " + banknote + " рублей: " + numberOfBanknotes + " шт." );
                }
            }
            if (acceptedSum < requiredSum) {
                throw new NotSupportedBanknotesSet(acceptedSum);
            } else {
                newStoredBanknotes.forEach((banknote, amount) -> {
                    storedBanknotes.put(banknote, amount);
                });
                System.out.println("Данный набор банкнот выдан");
            }
        } catch (NotEnoughMoneyException e) {
            e.printStackTrace();
        } catch (NotSupportedBanknotesSet notSupportedBanknotesSet) {
            notSupportedBanknotesSet.printStackTrace();
        }

        return map;
    }

    @Override
    public Map<Long, Integer> dispenseRest() {
        Map<Long, Integer> map = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<Long, Integer> entry : storedBanknotes.entrySet()) {
            System.out.println("Выдано банкнот номиналом " + entry.getKey() + " рублей: " + entry.getValue() + " шт." );
            map.put(entry.getKey(), entry.getValue());
            storedBanknotes.put(entry.getKey(), storedBanknotes.get(entry.getKey()) - entry.getValue());
        }
        return map;
    }

    public long getBalance() {
        long balance = 0;
        for (Map.Entry<Long, Integer> entry: storedBanknotes.entrySet()) {
            balance += entry.getKey() * entry.getValue();
        }
        return balance;
    }

    //в обоих диспенсерах вывод sout просто имитирует выдачу, чтобы было видно, что и сколько выдано.
    //а return возвращает Map - карту банкнот с указанным номиналом и кол-вом банкнот этого номинала - которую можно
    //использовать как данные для реального физического диспенсера, чтобы он знал - сколько чего выдать

}
