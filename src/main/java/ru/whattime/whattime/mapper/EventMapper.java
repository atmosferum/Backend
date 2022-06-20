package ru.whattime.whattime.mapper;

import org.mapstruct.Mapper;
import ru.whattime.whattime.dto.EventDTO;
import ru.whattime.whattime.model.Event;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper
public interface EventMapper {
    EventDTO toDTO(Event event);
    Event toEntity(EventDTO eventDto);

    default Long map(LocalDateTime value) {
        return value.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    default LocalDateTime map(Long value) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(value), ZoneId.systemDefault());
    }
}
