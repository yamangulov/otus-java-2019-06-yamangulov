package ru.otus;

import ru.otus.classes.ATM;
import ru.otus.classes.Cassette;
import ru.otus.classes.Department;
import ru.otus.enums.BanknotesRU;
import ru.otus.interfaces.IBanknotes;
import ru.otus.interfaces.IDepartment;

import java.util.HashMap;
import java.util.Map;

public class Start {
    public static void main(String[] args) {

        //начала рабочего дня, готовим банкоматы к работе
        IDepartment department = new Department();
        ATM atm1 = department.addATM();
        ATM atm2 = department.addATM();
        atm1.loadCassette(new Cassette(BanknotesRU.FIFTY.getValue(), 1000));
        atm2.loadSetCassettes();
        //запомним состояние департамента, реализуется паттерн memento
        department.rememberState();

        System.out.println(atm1.getBalance());
        System.out.println(atm2.getBalance());

        //создаем "пачку" банкнот, которую клиент вставляет в приемник банкнот
        Map<IBanknotes, Integer> map = new HashMap<>();
        map.put(BanknotesRU.FIFTY, 10);
        map.put(BanknotesRU.THOUSAND, 10);
        map.put(BanknotesRU.FIVE_THOUSEND, 3);
        //клиент загружает пачку банкнот в приемник банкомата
        atm2.accept(map);
        System.out.println(atm2.getBalance());

        //выдача требуемой суммы
        atm2.dispense(7350);
        System.out.println(atm2.getBalance());
        //здесь будет исключение - денег недостаточно
        atm2.dispense(7350000);
        System.out.println(atm2.getBalance());

        //забираем все деньги из банкоматов департамента, это mediator, я так думаю
        //мы не обращается напрямую к каждому отдельному банкомату, чтобы вызвать метод выдать остаток у него, а вызываем
        //общий метод выдать остаток для всего департамента, который вызывает отдельные методы у каждого банкомата сам
        //поэтому департамент - это объект-посредник для атм
        department.dispenseTotalRest();

        System.out.println(atm1.getBalance());
        System.out.println(atm2.getBalance());

        //восстанавливаем первоначальное состояние банкоматов департамента
        department.undoState();

        System.out.println(atm1.getBalance());
        System.out.println(atm2.getBalance());

    }
}
