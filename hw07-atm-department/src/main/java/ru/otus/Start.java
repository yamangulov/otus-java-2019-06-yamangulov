package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.classes.ATMImpl;
import ru.otus.classes.Cassette;
import ru.otus.classes.DepartmentImpl;
import ru.otus.classes.DispenserImpl;
import ru.otus.enums.BanknotesRU;
import ru.otus.interfaces.Banknotes;
import ru.otus.interfaces.Department;

import java.util.HashMap;
import java.util.Map;

public class Start {

    private static Logger logger = LoggerFactory.getLogger(Start.class);

    public static void main(String[] args) {

        //начала рабочего дня, готовим банкоматы к работе
        Department department = new DepartmentImpl();
        ATMImpl atm1 = department.addATM();
        ATMImpl atm2 = department.addATM();
        atm1.loadCassette(new Cassette(BanknotesRU.FIFTY.getValue(), 1000));
        atm2.loadSetCassettes();
        //запомним состояние департамента, реализуется паттерн memento
        department.rememberState();

        logger.debug(String.valueOf(atm1.getBalance()));
        logger.debug(String.valueOf(atm2.getBalance()));

        //создаем "пачку" банкнот, которую клиент вставляет в приемник банкнот
        Map<Banknotes, Integer> map = new HashMap<>();
        map.put(BanknotesRU.FIFTY, 10);
        map.put(BanknotesRU.THOUSAND, 10);
        map.put(BanknotesRU.FIVE_THOUSEND, 3);
        //клиент загружает пачку банкнот в приемник банкомата
        atm2.accept(map);
        logger.debug(String.valueOf(atm2.getBalance()));

        //выдача требуемой суммы
        atm2.dispense(7350);
        logger.debug(String.valueOf(atm2.getBalance()));
        //здесь будет исключение - денег недостаточно
        atm2.dispense(7350000);
        logger.debug(String.valueOf(atm2.getBalance()));

        //забираем все деньги из банкоматов департамента, это mediator, я так думаю
        //мы не обращается напрямую к каждому отдельному банкомату, чтобы вызвать метод выдать остаток у него, а вызываем
        //общий метод выдать остаток для всего департамента, который вызывает отдельные методы у каждого банкомата сам
        //поэтому департамент - это объект-посредник для атм
        department.dispenseTotalRest();

        logger.debug(String.valueOf(atm1.getBalance()));
        logger.debug(String.valueOf(atm2.getBalance()));

        //восстанавливаем первоначальное состояние банкоматов департамента
        department.undoState();

        logger.debug(String.valueOf(atm1.getBalance()));
        logger.debug(String.valueOf(atm2.getBalance()));

    }
}
