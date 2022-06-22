package ru.whattime.whattime.dto;

import lombok.Data;
import ru.whattime.whattime.model.User;

import javax.validation.constraints.NotNull;

@Data
public class IntervalDto {
    private Long id;

    private User owner;

    @NotNull
    private Long startTime;

    @NotNull
    private Long endTime;
}
