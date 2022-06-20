package ru.whattime.whattime.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@Jacksonized
public class EventDTO {
    private String uuid;

    @NotBlank
    private String title;

    private String description;

    private UserDTO owner;

    private Long createdAt;
}
