package ru.otus.classes;

import ru.otus.exceptions.NotEnoughMoneyException;
import ru.otus.interfaces.IDispenser;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Dispenser implements IDispenser {

    private Map<Long, Integer> storedBanknotes = new TreeMap<>(Collections.reverseOrder());

    public Map<Long, Integer> getStoredBanknotes() {
        return storedBanknotes;
    }

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


    /*
    Метод dispense для получения набора выдаваемых купюр сильно упрощенный, так как в ДЗ не ставилось задачи получить
    идеальную реализацию ATM, а только продемонстрировать применение паттернов. В реальности возможна ситуация, когда
    запрошенная сумма меньше остатка в банкомате, но все же подобрать набор купюр из оставшихся в банкомате в поле
    storedBanknotes невозможно. Тогда можно было бы реализовать еще одно исключение типа "банкомат не может выдать
    запрошенную сумму по техническим причинам, вы можете получить сумму xxx рублей" (подсчитывается ближайшая
    к запрошенной меньшая сумма, которая может быть выдана оставшимся в банкомате набором купюр). Но это достаточно
    сложно и выходит за рамки ДЗ
     */
    @Override
    public Map<Long, Integer> dispense(long requiredSum) {
        Map<Long, Integer> map = new TreeMap<>(Collections.reverseOrder());

        try {
            if (getBalance() < requiredSum) {
                throw new NotEnoughMoneyException();
            }
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
        } catch (NotEnoughMoneyException e) {
            e.printStackTrace();
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
