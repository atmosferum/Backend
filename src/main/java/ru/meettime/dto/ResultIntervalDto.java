package ru.meettime.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class ResultIntervalDto {
    private Long startTime;
    private Long endTime;
    private List<UserDto> owners;
}
