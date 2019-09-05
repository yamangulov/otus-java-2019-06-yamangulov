package ru.otus.enums;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BanknotesRUTest {

    @Test
    void getBanknote() {
        long value = 500;
        BanknotesRU result = BanknotesRU.getBanknote(value);
        assertEquals("FIVE_HUNDRED", result.toString());
    }
}