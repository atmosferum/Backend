package ru.meettime.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Data
@Jacksonized
@Builder
public class ErrorDto {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String path;
    private String message;
}
