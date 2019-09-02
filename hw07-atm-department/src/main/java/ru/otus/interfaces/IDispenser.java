package ru.otus.interfaces;

import java.util.Map;

public interface IDispenser {

    boolean accept(Map<Long, Integer> banknotes);

    Map<Long, Integer> dispense(long requiredSum);

    Map<Long, Integer> dispenseRest();

}
