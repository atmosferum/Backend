package ru.whattime.whattime.mapper;

import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper
public interface LocalDateTimeMapper {
    default Long map(LocalDateTime value) {
        return value == null? null : value.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    default LocalDateTime map(Long value) {
        return value == null ? null : LocalDateTime.ofInstant(Instant.ofEpochSecond(value), ZoneId.systemDefault());
    }
}