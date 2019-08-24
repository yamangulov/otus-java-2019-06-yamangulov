package ru.otus;

import ru.otus.classes.ATM_RU;
import ru.otus.classes.Cassette;
import ru.otus.enums.BanknotesRU;
import ru.otus.interfaces.IATM;
import ru.otus.interfaces.IBanknotes;

import java.util.HashMap;
import java.util.Map;

public class Start {
    public static void main(String[] args) {

        IATM atm = new ATM_RU();

        //загрузка кассеты 50 рублевок
        atm.loadCassette(new Cassette(BanknotesRU.FIFTY.getValue(), 1000));

        //загрузка полного набора кассет - всех номиналов по 1000 шт банкнот
        atm.loadSetCassettes();
        System.out.println(atm.getBalance());

        //создаем "пачку" банкнот, которую клиент вставляет в приемник банкнот
        Map<IBanknotes, Integer> map = new HashMap<>();
        map.put(BanknotesRU.FIFTY, 10);
        map.put(BanknotesRU.THOUSAND, 10);
        map.put(BanknotesRU.FIVE_THOUSEND, 3);
        //клиент загружает пачку банкнот в приемник банкомата
        atm.accept(map);
        System.out.println(atm.getBalance());

        //выдача требуемой суммы
        atm.dispense(7350);
        System.out.println(atm.getBalance());

        //выдача остатка в банкомате
        atm.dispenseRest();
        System.out.println(atm.getBalance());
    }
}
