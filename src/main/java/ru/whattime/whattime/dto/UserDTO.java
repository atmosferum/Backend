package ru.whattime.whattime.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {
    private Long id;

    @NotBlank
    private String name;
}
