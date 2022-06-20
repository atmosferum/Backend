package ru.whattime.whattime.dto;

import lombok.Data;

@Data
public class EventDTO {
    private String uuid;
    private String title;
    private String description;
    private UserDTO owner;
    private Long createdAt;
}
