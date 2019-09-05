package ru.otus.exceptions;

public class NotSupportedBanknotesSet extends Exception {

    private long acceptedSum;

    public NotSupportedBanknotesSet(long acceptedSum) {
        super();
        this.acceptedSum = acceptedSum;
    }

    @Override
    public String toString() {
        return super.toString() + "\nНевозможно выдать требуемую сумму, в банкомате недостаточно подходящих банкнот.\nВы можете получить " + acceptedSum + " рублей.";
    }
}
