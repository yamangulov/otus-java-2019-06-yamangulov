package ru.otus.classes;

import ru.otus.interfaces.IBanknotes;
import ru.otus.interfaces.IDepartment;

import java.util.HashMap;
import java.util.Map;

public class Department implements IDepartment {

    private StateMemory departmentState = new StateMemory();

    @Override
    public ATM addATM() {
        ATM atm = new ATM(departmentState);
        departmentState.rememberATM(atm);
        return atm;
    }

    @Override
    public ATM addATM(ATM atm) {
        departmentState.rememberATM(atm);
        return atm;
    }

    @Override
    public Map<IBanknotes, Integer> dispenseTotalRest() {
        Map<IBanknotes, Integer> totalRest = new HashMap<>();
        for (ATM atm : departmentState.getAtms()) {
            Map<IBanknotes, Integer> rest = atm.dispenseRest();
            rest.forEach(((banknote, amount) -> {
                Integer initialAmount;
                if (totalRest.get(banknote) != null) {
                    initialAmount = totalRest.get(banknote);
                } else {
                    initialAmount = 0;
                }
                totalRest.put(banknote, initialAmount + amount);
            }));
        }
        for (Map.Entry<IBanknotes, Integer> entry : totalRest.entrySet()) {
            System.out.println("Итого из всех банкоматов выдано банкнот номиналом " + entry.getKey().getValue() + " рублей: " + entry.getValue() + " шт." );
        }
        return totalRest;
    }


    @Override
    public void undoState() {
        for (ATM atm : departmentState.getInitAtms()) {
            if (!departmentState.getAtms().contains(atm)) {
                this.addATM(atm);
            }
        }
        for (ATM atm : departmentState.getAtms()) {
            Map<Long, Integer> cassettes = departmentState.getInitAtmCassetes().get(atm);
            for (Map.Entry<Long, Integer> cassette : cassettes.entrySet()) {
                atm.loadCassette(new Cassette(cassette.getKey(), cassette.getValue()));
            }
        }
        departmentState.setAtms(departmentState.getInitAtms());
        departmentState.setAtmCassetes(departmentState.getInitAtmCassetes());
    }

    @Override
    public void rememberState() {
        departmentState.setInitAtms(departmentState.getAtms());
        departmentState.setInitAtmCassetes(departmentState.getAtmCassetes());
    }
}
