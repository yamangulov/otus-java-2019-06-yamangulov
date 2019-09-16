package ru.otus.classes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.classes.memento.Caretaker;
import ru.otus.classes.memento.Originator;
import ru.otus.interfaces.Banknotes;
import ru.otus.interfaces.Department;

import java.util.HashMap;
import java.util.Map;

public class DepartmentImpl implements Department {

    private static Logger logger = LoggerFactory.getLogger(DepartmentImpl.class);

    private Originator originator = new Originator();
    private Caretaker caretaker = new Caretaker();

    public Originator getOriginator() {
        return originator;
    }




    @Override
    public ATMImpl addATM() {
        ATMImpl atm = new ATMImpl(originator);
        originator.rememberATM(atm);
        return atm;
    }

    @Override
    public ATMImpl addATM(ATMImpl atm) {
        originator.rememberATM(atm);
        return atm;
    }

    @Override
    public Map<Banknotes, Integer> dispenseTotalRest() {
        Map<Banknotes, Integer> totalRest = new HashMap<>();
        for (ATMImpl atm : originator.getAtmsState()) {
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
    public void restoreState() {
        originator.restoreState(caretaker.getMemento());
        for (ATMImpl atm : originator.getAtmsState()) {
            if (!originator.getAtmsState().contains(atm)) {
                this.addATM(atm);
            }
        }
        for (ATMImpl atm : originator.getAtmsState()) {
            Map<Long, Integer> cassettes = originator.getAtmCassettesState().get(atm);
            for (Map.Entry<Long, Integer> cassette : cassettes.entrySet()) {
                atm.loadCassette(new Cassette(cassette.getKey(), cassette.getValue()));
            }
        }
    }

    @Override
    public void saveState() {
        caretaker.setMemento(originator.saveState());
    }

    //используется, когда хочется сохранить промежуточное состояние департамента, а не состояние в начале дня
    //в Start не использован, но есть использование в тесте для этого метода
    @Override
    public void saveModifiedState() {
        caretaker.setMemento(originator.saveModifiedState(caretaker.getMemento()));
    }
}
