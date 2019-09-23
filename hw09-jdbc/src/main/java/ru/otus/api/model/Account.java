package ru.otus.api.model;

import ru.otus.annotations.Id;
import ru.otus.annotations.Size;
import ru.otus.annotations.Type;
import ru.otus.jdbc.helpers.H2DataType;

import java.math.BigDecimal;

public class Account {

    @Id
    @Type(type = H2DataType.BIGINT)
    @Size(size = 20)
    private long no;

    @Type
    @Size
    private String type;

    @Type(type = H2DataType.NUMBER)
    private BigDecimal number;

    public Account(long no, String type, BigDecimal number) {
        this.no = no;
        this.type = type;
        this.number = number;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }
}
