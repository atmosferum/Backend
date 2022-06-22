package ru.whattime.whattime.validation;

import ru.whattime.whattime.dto.IntervalDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Comparator;
import java.util.List;

public class NotOverlayConstraintValidator implements ConstraintValidator<NotOverlay, List<IntervalDto>> {

    @Override
    public boolean isValid(List<IntervalDto> value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        value.sort(Comparator.comparing(IntervalDto::getStartTime));
        for (int i = 0; i < value.size() - 1; i++) {
            Long endOfEarlier = value.get(i).getEndTime();
            Long startOfLatter = value.get(i + 1).getStartTime();
            if (endOfEarlier == null || startOfLatter == null || endOfEarlier >= startOfLatter) {
                return false;
            }
        }
        return true;
    }
}
