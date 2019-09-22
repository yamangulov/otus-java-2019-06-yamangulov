package ru.otus.jdbc.helpers;

public enum H2DataType {
    INT("INT"),
    BIGINT("BIGINT"),
    VARCHAR("VARCHAR"),
    NUMBER("DECIMAL");

    private String value;

    H2DataType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
