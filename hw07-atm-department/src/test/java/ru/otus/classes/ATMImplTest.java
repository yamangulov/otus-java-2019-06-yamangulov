package ru.otus.classes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.enums.BanknotesRU;
import ru.otus.interfaces.Banknotes;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ATMImplTest {

    @Test
    void loadCassette() {
        ATMImpl atm = new ATMImpl();
        Cassette cassette = new Cassette((long)5000, 1000);
        atm.loadCassette(cassette);
        long balance = atm.getBalance();
        assertEquals(5_000_000, balance);
    }

    @Test
    void loadSetCassettes() {
        ATMImpl atm = new ATMImpl();
        atm.loadSetCassettes();
        long balance = atm.getBalance();
        //всех банкнот по 1 кассете по 1000 штук в кассете
        assertEquals(6_650_000, balance);
    }


    @Test
    void getBalance() {
        ATMImpl atm = new ATMImpl();
        atm.loadSetCassettes();
        assertEquals(atm.getDispenser().getBalance(), atm.getBalance());
    }

    @Test
    void accept() {
        ATMImpl atm = new ATMImpl();

        Map<Long, Integer> banknotes = new HashMap<>();
        banknotes.put((long)5000, 1000);
        banknotes.put((long)1000, 1000);
        atm.getDispenser().accept(banknotes);
        long balance1 = atm.getBalance();

        Map<Banknotes, Integer> banknotesEnums = new HashMap<>();
        banknotesEnums.put(BanknotesRU.FIVE_THOUSEND, 1000);
        banknotesEnums.put(BanknotesRU.THOUSAND, 1000);
        atm.accept(banknotesEnums);
        long balance2 = atm.getBalance();

        assertEquals(balance1, balance2 - balance1);
    }

    @Test
    void dispense() {
        ATMImpl atm = new ATMImpl();
        atm.loadSetCassettes();
        long initBalance = atm.getBalance();
        atm.dispense(1000);
        long firstBalance = atm.getBalance();
        atm.getDispenser().dispense(1000);
        long secondBalance = atm.getBalance();
        assertEquals(initBalance - firstBalance, firstBalance - secondBalance);
    }

    @Test
    void dispenseRest() {
        ATMImpl atm = new ATMImpl();

        atm.loadSetCassettes();
        Map<Banknotes, Integer> gettedByATMMethod = atm.dispenseRest();
        long sumFromATM = 0;
        for (Map.Entry<Banknotes, Integer> entry : gettedByATMMethod.entrySet()) {
            sumFromATM += entry.getKey().getValue() * entry.getValue();
        }

        atm.loadSetCassettes();
        Map<Long, Integer> gettedMyDispenserMethod = atm.getDispenser().dispenseRest();
        long sumFromDispenser = 0;
        for (Map.Entry<Long, Integer> entry : gettedMyDispenserMethod.entrySet()) {
            sumFromDispenser += entry.getKey() * entry.getValue();
        }

        assertEquals(sumFromDispenser, sumFromATM);

    }
}