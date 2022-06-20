package ru.whattime.whattime.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EventDTO {
    private String uuid;

    @NotBlank
    private String title;

    private String description;

    private UserDTO owner;

    private Long createdAt;
}
