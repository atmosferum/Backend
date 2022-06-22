package ru.whattime.whattime.dto;

import lombok.Data;
import ru.whattime.whattime.model.User;
import ru.whattime.whattime.validation.StartBeforeEnd;

import javax.validation.constraints.NotNull;

@Data
@StartBeforeEnd
public class IntervalDto {

    private Long id;
    private UserDto owner;

    @NotNull(message = "Start time of an interval must not be null")
    private Long startTime;

    @NotNull(message = "End time of an interval must not be null")
    private Long endTime;

}
