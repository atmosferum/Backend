package ru.meettime.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.meettime.validation.StartBeforeEnd;

import javax.validation.constraints.NotNull;

@Data
@StartBeforeEnd
@NoArgsConstructor
public class IntervalDto {

    private Long id;
    private UserDto owner;

    @NotNull(message = "Start time of an interval must not be null")
    private Long startTime;

    @NotNull(message = "End time of an interval must not be null")
    private Long endTime;

    public IntervalDto(Long startTime, Long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
