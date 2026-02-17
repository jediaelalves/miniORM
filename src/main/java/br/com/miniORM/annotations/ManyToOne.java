package br.com.miniORM.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToOne {
    // Você pode adicionar parâmetros depois se quiser, por exemplo:
    // fetch() default FetchType.LAZY;
    // cascade() default {};
}
