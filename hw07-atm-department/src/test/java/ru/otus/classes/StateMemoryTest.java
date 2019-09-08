package ru.otus.classes;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StateMemoryTest {

    @Test
    void rememberATM() {
        StateMemory stateMemory = new StateMemory();
        ATMImpl atm = new ATMImpl();
        ATMImpl atm2 = new ATMImpl();
        stateMemory.rememberATM(atm);
        stateMemory.rememberATM(atm2);
        Set<ATMImpl> atmsFromStateMemory = stateMemory.getAtms();
        assertTrue(atmsFromStateMemory.contains(atm));
        assertTrue(atmsFromStateMemory.contains(atm2));
    }

    @Test
    void rememberCassetteToATM() {
        StateMemory stateMemory = new StateMemory();
        ATMImpl atm = new ATMImpl();
        Cassette cassette = new Cassette(1000, 1000);
        stateMemory.rememberATM(atm);
        stateMemory.rememberCassetteToATM(cassette, atm);
        long cassetteValue = cassette.getValue();
        int casseteAmount = cassette.getAmount();
        Map<Long, Integer> atmCassettesFromStateMemory = stateMemory.getAtmCassetes().get(atm);
        assertTrue(atmCassettesFromStateMemory.containsKey(cassetteValue));
        assertEquals(casseteAmount, (int)atmCassettesFromStateMemory.get(cassetteValue));
    }
}