package ru.whattime.whattime.validation;

import ru.whattime.whattime.dto.IntervalDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

public class RightIntervalsConstraintValidator implements ConstraintValidator<RightIntervals, List<IntervalDto>> {
    @Override
    public boolean isValid(List<IntervalDto> intervalDtos, ConstraintValidatorContext constraintValidatorContext) {
        intervalDtos.sort(Comparator.comparing((IntervalDto::getStartTime)));

        for (int i = 0; i < intervalDtos.size() - 1; i++) {
            IntervalDto interval = intervalDtos.get(i);

            LocalDateTime startTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(interval.getStartTime()), ZoneId.systemDefault());
            LocalDateTime endTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(interval.getEndTime()), ZoneId.systemDefault());
            LocalDateTime startTimeNext = LocalDateTime.ofInstant(Instant.ofEpochSecond(intervalDtos.get(i + 1).getStartTime()), ZoneId.systemDefault());

            if (startTimeNext.isBefore(endTime)) {
                return false;
            }

            if (endTime.isBefore(startTime) && startTime.isBefore(LocalDateTime.now())) {
                return false;
            }

            if (i != intervalDtos.size()) {
                if (endTime.isBefore(startTimeNext)) {
                    return false;
                }
            }
        }

        return true;
    }
}