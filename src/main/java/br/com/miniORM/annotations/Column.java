package br.com.miniORM.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    String name() default ""; // 🔥 ESSENCIAL para mapear coluna diferente do atributo

    int length() default 255;
    boolean nullable() default true;
}
