package ru.whattime.whattime.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    private Long id;

    @NotBlank
    @Size(min = 1, max = 63)
    private String name;
}
