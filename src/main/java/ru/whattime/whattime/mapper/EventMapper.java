package ru.whattime.whattime.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.whattime.whattime.dto.EventDto;
import ru.whattime.whattime.model.Event;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(uses = UserMapper.class)
public interface EventMapper {

    @Mapping(source = "created", target = "createdAt")
    @Mapping(source = "uuid", target = "id")
    EventDto toDto(Event event);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "intervals", ignore = true)
    Event toEntity(EventDto eventDto);

    default Long map(LocalDateTime value) {
        return value == null? null : value.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    default LocalDateTime map(Long value) {
        return value == null ? null : LocalDateTime.ofInstant(Instant.ofEpochSecond(value), ZoneId.systemDefault());
    }
}
