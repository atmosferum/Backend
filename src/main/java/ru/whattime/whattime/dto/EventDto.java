package ru.whattime.whattime.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class EventDto {
    private String uuid;

    @NotBlank(message = "Event must have a title")
    @Size(min = 1, max = 63, message = "Title's length of an event must be between 1 and 63")
    private String title;

    private String description;
    private UserDto owner;
    private Long createdAt;
}
