package ru.whattime.whattime.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    private Long id;

    @NotBlank(message = "User's name must not be blank")
    @Size(min = 1, max = 63, message = "User's name length should be between 1 and 63")
    private String name;
}
