package ru.otus.classes;

import org.junit.jupiter.api.Test;
import ru.otus.interfaces.Banknotes;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentImplTest {

    @Test
    void dispenseTotalRest() {
        DepartmentImpl department = new DepartmentImpl();
        ATMImpl atm1 = department.addATM();
        ATMImpl atm2 = department.addATM();
        Cassette cassette1 = new Cassette(1000, 1000);
        Cassette cassette2 = new Cassette(5000, 1000);
        atm1.loadCassette(cassette1);
        atm2.loadCassette(cassette2);
        Map<Banknotes, Integer> rest = department.dispenseTotalRest();
        long sumRest = 0;
        for (Map.Entry<Banknotes, Integer> entry : rest.entrySet()) {
            sumRest += entry.getKey().getValue() * entry.getValue();
        }
        assertEquals(6_000_000, sumRest);
    }

    @Test
    void undoState() {
        DepartmentImpl department = new DepartmentImpl();
        ATMImpl atm1 = department.addATM();
        ATMImpl atm2 = department.addATM();
        ATMImpl atm3 = new ATMImpl();
        Cassette cassette1 = new Cassette(1000, 1000);
        Cassette cassette2 = new Cassette(5000, 1000);
        atm1.loadCassette(cassette1);
        atm2.loadCassette(cassette2);
        department.rememberState();
        atm1.dispense(10000);
        atm2.dispense(50000);
        department.undoState();
        Set<ATMImpl> atmsFromMemory = department.getDepartmentState().getInitAtms();
        Map<ATMImpl, Map<Long, Integer>> atmCassettesMemory = department.getDepartmentState().getInitAtmCassetes();
        assertTrue(atmsFromMemory.contains(atm1));
        assertTrue(atmsFromMemory.contains(atm2));
        assertFalse(atmsFromMemory.contains(atm3));
        assertTrue(atmCassettesMemory.get(atm1).containsKey(Long.valueOf(1000)));
        assertFalse(atmCassettesMemory.get(atm1).containsKey(Long.valueOf(5000)));
        assertFalse(atmCassettesMemory.get(atm1).containsKey(Long.valueOf(500)));
        assertEquals(cassette1.getAmount(), (long)atmCassettesMemory.get(atm1).get(Long.valueOf(1000)));
        assertEquals(cassette2.getAmount(), (long)atmCassettesMemory.get(atm2).get(Long.valueOf(5000)));
    }

    @Test
    void rememberState() {
        DepartmentImpl department = new DepartmentImpl();
        ATMImpl atm1 = department.addATM();
        ATMImpl atm2 = department.addATM();
        ATMImpl atm3 = new ATMImpl();
        Cassette cassette1 = new Cassette(1000, 1000);
        Cassette cassette2 = new Cassette(5000, 1000);
        atm1.loadCassette(cassette1);
        atm2.loadCassette(cassette2);
        department.rememberState();
        Set<ATMImpl> atmsFromMemory = department.getDepartmentState().getInitAtms();
        Map<ATMImpl, Map<Long, Integer>> atmCassettesMemory = department.getDepartmentState().getInitAtmCassetes();
        assertTrue(atmsFromMemory.contains(atm1));
        assertTrue(atmsFromMemory.contains(atm2));
        assertFalse(atmsFromMemory.contains(atm3));
        assertTrue(atmCassettesMemory.get(atm1).containsKey(Long.valueOf(1000)));
        assertFalse(atmCassettesMemory.get(atm1).containsKey(Long.valueOf(5000)));
        assertFalse(atmCassettesMemory.get(atm1).containsKey(Long.valueOf(500)));
        assertEquals(cassette1.getAmount(), (long)atmCassettesMemory.get(atm1).get(Long.valueOf(1000)));
        assertEquals(cassette2.getAmount(), (long)atmCassettesMemory.get(atm2).get(Long.valueOf(5000)));
    }
}