package ru.whattime.whattime.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.whattime.whattime.serializer.UuidSerializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class EventDto {

    @JsonProperty("id")
    @JsonSerialize(using = UuidSerializer.class)
    private UUID uuid;

    @NotBlank
    @Size(min = 1, max = 63)
    private String title;

    private String description;

    private UserDto owner;

    private Long createdAt;
}
