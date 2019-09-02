package ru.otus.enums;

import ru.otus.interfaces.IBanknotes;

import java.util.HashMap;

public enum BanknotesRU implements IBanknotes {
    FIFTY(50),
    HUNDRED(100),
    FIVE_HUNDRED(500),
    THOUSAND(1000),
    FIVE_THOUSEND(5000);

    private final long value;

    BanknotesRU(long value) {
        this.value = value;
    }


    @Override
    public long getValue() {
        return value;
    }

    private static HashMap<Long, BanknotesRU> names() {
        HashMap<Long, BanknotesRU> names = new HashMap<>();
        BanknotesRU[] enums = BanknotesRU.values();
        for (BanknotesRU name : enums) {
            names.put(name.getValue(), name);
        }
        return names;
    }

    public static BanknotesRU getBanknote(long valueOfBanknote) {
        return BanknotesRU.names().get(valueOfBanknote);
    }


}
