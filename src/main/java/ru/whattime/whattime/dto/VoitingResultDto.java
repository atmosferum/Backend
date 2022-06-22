package ru.whattime.whattime.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class VoitingResultDto {
    private EventDto event;
    private List<IntervalDto> intervals;
    private Set<UserDto> participants;
}
