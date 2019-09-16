package ru.otus.classes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.exceptions.NotEnoughMoneyException;
import ru.otus.exceptions.NotSupportedBanknotesSet;
import ru.otus.interfaces.Dispenser;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class DispenserImpl implements Dispenser {

    private static Logger logger = LoggerFactory.getLogger(DispenserImpl.class);

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
                String message = "Принято банкнот номиналом " + banknote + " рублей: " + amount + " шт.";
                logger.debug(message);
            }));
            return true;
        } catch (Exception e) {
            logger.error("Error in method DispenserImpl.accept(): ", e);
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
                    String message = "Может быть выдано банкнот номиналом " + banknote + " рублей: " + numberOfBanknotes + " шт.";
                    logger.debug(message);
                }
            }
            if (acceptedSum < requiredSum) {
                throw new NotSupportedBanknotesSet(acceptedSum);
            } else {
                newStoredBanknotes.forEach((banknote, amount) -> {
                    storedBanknotes.put(banknote, amount);
                });
                String message = "Данный набор банкнот выдан";
                logger.debug(message);
            }
        } catch (NotEnoughMoneyException e) {
            logger.error("Error in method DispenserImpl.dispense(): ", e);
        } catch (NotSupportedBanknotesSet notSupportedBanknotesSet) {
            logger.error("Error in method DispenserImpl.dispense(): ", notSupportedBanknotesSet);
        }

        return map;
    }

    @Override
    public Map<Long, Integer> dispenseRest() {
        Map<Long, Integer> map = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<Long, Integer> entry : storedBanknotes.entrySet()) {
            String message = "Выдано банкнот номиналом " + entry.getKey() + " рублей: " + entry.getValue() + " шт.";
            logger.debug(message);
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

    //return возвращает Map - карту банкнот с указанным номиналом и кол-вом банкнот этого номинала - которую можно
    //использовать как данные для реального физического диспенсера, чтобы он знал - сколько чего выдать

}
