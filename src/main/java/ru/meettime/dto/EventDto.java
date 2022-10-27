package ru.meettime.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.meettime.converter.StringToUUIDConverter;
import ru.meettime.converter.UUIDToStringConverter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class EventDto {

    @JsonProperty("id")
    @JsonSerialize(converter = UUIDToStringConverter.class)
    @JsonDeserialize(converter = StringToUUIDConverter.class)
    private UUID uuid;

    @NotBlank(message = "Event must have a title")
    @Size(min = 1, max = 63, message = "Title's length of an event must be between 1 and 63")
    private String title;

    private String description;
    private UserDto owner;
    private Long createdAt;
}
