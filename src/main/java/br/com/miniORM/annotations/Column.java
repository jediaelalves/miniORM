package br.com.miniORM.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    int length() default 255;
    boolean nullable() default true;
}
