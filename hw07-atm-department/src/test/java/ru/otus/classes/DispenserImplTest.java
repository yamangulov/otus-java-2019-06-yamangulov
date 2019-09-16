package ru.otus.classes;

import org.junit.jupiter.api.Test;
import ru.otus.exceptions.NotEnoughMoneyException;
import ru.otus.exceptions.NotSupportedBanknotesSet;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DispenserImplTest {

    @Test
    void accept() {
        DispenserImpl dispenser = new DispenserImpl();
        Map<Long, Integer> banknotes = new HashMap<>();
        long value = 5000;
        banknotes.put(value, 1000);
        value = 1000;
        banknotes.put(value, 550);
        dispenser.accept(banknotes);
        dispenser.getStoredBanknotes().forEach((banknote, amount) -> {
            assertEquals(amount, banknotes.get(banknote));
        });
    }

    @Test
    void dispense() {
        DispenserImpl dispenser = new DispenserImpl();
        Map<Long, Integer> banknotes = new HashMap<>();
        long value = 5000;
        banknotes.put(value, 1000);
        value = 1000;
        banknotes.put(value, 550);
        dispenser.accept(banknotes);

        long requiredSum = 7_500_000;
        dispenser.dispense(requiredSum);
        assertThrows(NotEnoughMoneyException.class, () -> {
            throw new NotEnoughMoneyException();
        });

        final long requiredSum2 = 7_350;
        dispenser.dispense(requiredSum2);
        assertThrows(NotSupportedBanknotesSet.class, () -> {
            throw new NotSupportedBanknotesSet(requiredSum2);
        });

        long requiredSum3 = 8_000;
        long accumSum = 0;
        Map<Long, Integer> map = dispenser.dispense(requiredSum3);
        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            accumSum += entry.getKey() * entry.getValue();
        }
        assertEquals(requiredSum3, accumSum);


    }

    @Test
    void dispenseRest() {
        DispenserImpl dispenser = new DispenserImpl();
        Map<Long, Integer> banknotes = new HashMap<>();
        long value = 5000;
        banknotes.put(value, 1000);
        value = 1000;
        banknotes.put(value, 550);
        dispenser.accept(banknotes);

        long balance = dispenser.getBalance();
        Map<Long, Integer> map = dispenser.dispenseRest();
        long gettedRest = 0;
        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            gettedRest += entry.getKey() * entry.getValue();
        }
        assertEquals(balance, gettedRest);
    }

    @Test
    void getBalance() {
        DispenserImpl dispenser = new DispenserImpl();
        Map<Long, Integer> banknotes = new HashMap<>();
        long value = 5000;
        banknotes.put(value, 1000);
        value = 1000;
        banknotes.put(value, 550);
        dispenser.accept(banknotes);

        long balance = dispenser.getBalance();
        assertEquals(balance, 5550000);
    }
}