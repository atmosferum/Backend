package ru.whattime.whattime.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;
import java.util.regex.Pattern;

public class UuidValidator implements ConstraintValidator<IsUuid, String> {

    private final Pattern UUID_REGEX_PATTERN = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

    @Override
    public boolean isValid(String uuid, ConstraintValidatorContext constraintValidatorContext) {
        if (uuid == null) {
            return false;
        }
        return UUID_REGEX_PATTERN.matcher(uuid).matches();
    }
}
