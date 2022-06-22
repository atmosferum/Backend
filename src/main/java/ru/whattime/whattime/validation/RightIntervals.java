package ru.whattime.whattime.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RightIntervalsConstraintValidator.class)
@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RightIntervals {
    String message() default "Bad intervals content";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
