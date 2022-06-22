package ru.whattime.whattime.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StartBeforeEndConstraintValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartBeforeEnd {
    String message() default "Start of the interval is not before it's end";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

