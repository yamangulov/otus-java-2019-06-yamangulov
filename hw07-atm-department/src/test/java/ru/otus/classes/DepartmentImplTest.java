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
    void restoreState() {
        DepartmentImpl department = new DepartmentImpl();
        ATMImpl atm1 = department.addATM();
        ATMImpl atm2 = department.addATM();
        ATMImpl atm3 = new ATMImpl();
        Cassette cassette1 = new Cassette(1000, 1000);
        Cassette cassette2 = new Cassette(5000, 1000);
        atm1.loadCassette(cassette1);
        atm2.loadCassette(cassette2);
        department.saveState();
        atm1.dispense(10000);
        atm2.dispense(50000);
        department.restoreState();
        Set<ATMImpl> atmsFromMemory = department.getOriginator().getAtmsState();
        Map<ATMImpl, Map<Long, Integer>> atmCassettesMemory = department.getOriginator().getAtmCassettesState();
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
    void saveState() {
        DepartmentImpl department = new DepartmentImpl();
        ATMImpl atm1 = department.addATM();
        ATMImpl atm2 = department.addATM();
        ATMImpl atm3 = new ATMImpl();
        Cassette cassette1 = new Cassette(1000, 1000);
        Cassette cassette2 = new Cassette(5000, 1000);
        atm1.loadCassette(cassette1);
        atm2.loadCassette(cassette2);
        department.saveState();
        Set<ATMImpl> atmsFromMemory = department.getOriginator().getAtmsState();
        Map<ATMImpl, Map<Long, Integer>> atmCassettesMemory = department.getOriginator().getAtmCassettesState();
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
    void saveModifiedState() {
        DepartmentImpl department = new DepartmentImpl();
        ATMImpl atm1 = department.addATM();
        ATMImpl atm2 = department.addATM();
        ATMImpl atm3 = new ATMImpl();
        Cassette cassette1 = new Cassette(1000, 500);
        Cassette cassette2 = new Cassette(5000, 500);
        atm1.loadCassette(cassette1);
        atm2.loadCassette(cassette2);
        department.saveState();
        Cassette cassette3 = new Cassette(1000, 1000);
        Cassette cassette4 = new Cassette(5000, 1000);
        atm1.loadCassette(cassette3);
        atm2.loadCassette(cassette4);
        department.saveModifiedState();
        Set<ATMImpl> atmsFromMemory = department.getOriginator().getAtmsState();
        Map<ATMImpl, Map<Long, Integer>> atmCassettesMemory = department.getOriginator().getAtmCassettesState();
        assertTrue(atmsFromMemory.contains(atm1));
        assertTrue(atmsFromMemory.contains(atm2));
        assertFalse(atmsFromMemory.contains(atm3));
        assertTrue(atmCassettesMemory.get(atm1).containsKey(Long.valueOf(1000)));
        assertFalse(atmCassettesMemory.get(atm1).containsKey(Long.valueOf(5000)));
        assertFalse(atmCassettesMemory.get(atm1).containsKey(Long.valueOf(500)));
        assertEquals(cassette3.getAmount(), (long)atmCassettesMemory.get(atm1).get(Long.valueOf(1000)));
        assertEquals(cassette4.getAmount(), (long)atmCassettesMemory.get(atm2).get(Long.valueOf(5000)));
    }
}