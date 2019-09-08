package ru.otus.classes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.interfaces.Banknotes;
import ru.otus.interfaces.Department;

import java.util.HashMap;
import java.util.Map;

public class DepartmentImpl implements Department {

    private static Logger logger = LoggerFactory.getLogger(DepartmentImpl.class);

    private StateMemory departmentState = new StateMemory();

    public StateMemory getDepartmentState() {
        return departmentState;
    }

    @Override
    public ATMImpl addATM() {
        ATMImpl atm = new ATMImpl(departmentState);
        departmentState.rememberATM(atm);
        return atm;
    }

    @Override
    public ATMImpl addATM(ATMImpl atm) {
        departmentState.rememberATM(atm);
        return atm;
    }

    @Override
    public Map<Banknotes, Integer> dispenseTotalRest() {
        Map<Banknotes, Integer> totalRest = new HashMap<>();
        for (ATMImpl atm : departmentState.getAtms()) {
            Map<Banknotes, Integer> rest = atm.dispenseRest();
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
        for (Map.Entry<Banknotes, Integer> entry : totalRest.entrySet()) {
            String message = "Итого из всех банкоматов выдано банкнот номиналом " + entry.getKey().getValue() + " рублей: " + entry.getValue() + " шт.";
            logger.debug(message);
        }
        return totalRest;
    }


    @Override
    public void undoState() {
        for (ATMImpl atm : departmentState.getInitAtms()) {
            if (!departmentState.getAtms().contains(atm)) {
                this.addATM(atm);
            }
        }
        for (ATMImpl atm : departmentState.getAtms()) {
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
