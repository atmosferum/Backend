package ru.whattime.whattime.validator;

import org.springframework.stereotype.Component;
import ru.whattime.whattime.dto.IntervalDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

@Component
public class IntervalsValidator {
    public boolean validate(List<IntervalDto> intervals) {
        intervals.sort(Comparator.comparing((IntervalDto::getStartTime)));

        for (int i = 0; i < intervals.size(); i++) {
            IntervalDto interval = intervals.get(i);

            LocalDateTime startTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(interval.getStartTime()), ZoneId.systemDefault());
            LocalDateTime endTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(interval.getEndTime()), ZoneId.systemDefault());

            if (endTime.isBefore(startTime)) {
                return false;
            }

            if (startTime.isBefore(LocalDateTime.now())) {
                return false;
            }

            if (i != intervals.size()) {
                LocalDateTime startTimeNext = LocalDateTime.ofInstant(Instant.ofEpochSecond(intervals.get(i + 1).getStartTime()), ZoneId.systemDefault());
                if (endTime.isBefore(startTimeNext)) {
                    return false;
                }
            }
        }

        return true;
    }
}
