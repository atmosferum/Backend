package ru.whattime.whattime.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class VotingResultDto {
    private EventDto event;
    private List<ResultIntervalDto> intervals;
    private List<UserDto> participants;
}
