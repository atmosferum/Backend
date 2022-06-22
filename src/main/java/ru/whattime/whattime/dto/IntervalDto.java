package ru.whattime.whattime.dto;

import lombok.Data;
import ru.whattime.whattime.model.User;
import ru.whattime.whattime.validation.StartBeforeEnd;

import javax.validation.constraints.NotNull;

@Data
@StartBeforeEnd
public class IntervalDto {

    private Long id;
    private User owner;

    @NotNull
    private Long startTime;

    @NotNull
    private Long endTime;

}
