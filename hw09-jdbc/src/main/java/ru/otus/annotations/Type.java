package ru.otus.annotations;

import ru.otus.jdbc.helpers.H2DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Type {
    H2DataType type() default H2DataType.VARCHAR;
}
