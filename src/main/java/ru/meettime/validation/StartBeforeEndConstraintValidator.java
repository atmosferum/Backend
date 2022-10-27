package ru.meettime.validation;

import ru.meettime.dto.IntervalDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartBeforeEndConstraintValidator implements ConstraintValidator<StartBeforeEnd, IntervalDto> {

    @Override
    public boolean isValid(IntervalDto value, ConstraintValidatorContext context) {
        return value.getStartTime() != null
                && value.getEndTime() != null
                && value.getEndTime() > value.getStartTime();
    }
}

