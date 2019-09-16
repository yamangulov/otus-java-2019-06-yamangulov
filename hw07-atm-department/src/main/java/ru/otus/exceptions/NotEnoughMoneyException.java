package ru.otus.exceptions;

public class NotEnoughMoneyException extends Exception {
    @Override
    public String toString() {
        return super.toString() + "\nВ банкомате недостаточно наличных, чтобы выдать требуемую сумму";
    }
}
