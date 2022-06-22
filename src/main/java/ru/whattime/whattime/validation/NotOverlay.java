package ru.whattime.whattime.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotOverlayConstraintValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotOverlay {
    String message() default "Intervals overlay";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
