package ru.whattime.whattime.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class VotingCandidateDto {
    private IntervalDto interval;
    private List<UserDto> participants;
}
