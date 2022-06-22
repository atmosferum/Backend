package ru.whattime.whattime.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UuidValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsUuid {
    String message() default "Check if uuid is correct";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
