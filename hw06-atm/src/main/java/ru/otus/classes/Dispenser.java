package ru.otus.classes;

import ru.otus.interfaces.IDispenser;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Dispenser implements IDispenser {

    Map<Long, Integer> storedBanknotes = new TreeMap<>(Collections.reverseOrder());

    @Override
    public boolean accept(Map<Long, Integer> banknotes) {
        try {
            banknotes.forEach(((banknote, amount) -> {
                storedBanknotes.put(banknote, storedBanknotes.get(banknote) + amount);
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
        long sum = requiredSum;
        for (Map.Entry<Long, Integer> entry : storedBanknotes.entrySet()) {
            Long banknote = entry.getKey();
            Integer amount = entry.getValue();
            int numberOfBanknotes = (int) (sum / banknote);
            while (numberOfBanknotes > amount) {
                numberOfBanknotes--;
            }
            sum = sum - numberOfBanknotes * banknote;
            if (numberOfBanknotes > 0) {
                map.put(banknote, numberOfBanknotes);
                storedBanknotes.put(banknote, storedBanknotes.get(banknote) - numberOfBanknotes);
                System.out.println("Выдано банкнот номиналом " + banknote + " рублей: " + numberOfBanknotes + " шт." );
            }

        }
        return map;
    }

    @Override
    public Map<Long, Integer> dispenseRest() {
        Map<Long, Integer> rest = storedBanknotes;
        for (Map.Entry<Long, Integer> entry : storedBanknotes.entrySet()) {
            System.out.println("Выдано банкнот номиналом " + entry.getKey() + " рублей: " + entry.getValue() + " шт." );
            storedBanknotes.put(entry.getKey(), storedBanknotes.get(entry.getKey()) - entry.getValue());
        }
        return rest;
    }

    //в обоих диспенсерах вывод sout просто имитирует выдачу, чтобы было видно, что и сколько выдано.
    //а return возвращает Map - карту банкнот с указанным номиналом и кол-вом банкнот этого номинала - которую можно
    //использовать как данные для реального физического диспенсера, чтобы он знал - сколько чего выдать

}
