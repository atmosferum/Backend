package ru.whattime.whattime.converter;

import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper
public interface LocalDateTimeToLongConverter {
    default Long map(LocalDateTime value) {
        return value == null? null : value.atZone(ZoneId.systemDefault()).toEpochSecond();
    }
}
