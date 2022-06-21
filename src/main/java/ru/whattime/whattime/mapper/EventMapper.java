package ru.whattime.whattime.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.whattime.whattime.converter.LocalDateTimeMapper;
import ru.whattime.whattime.dto.EventDto;
import ru.whattime.whattime.model.Event;

@Mapper(uses = {UserMapper.class, LocalDateTimeMapper.class})
public interface EventMapper {

    @Mapping(source = "created", target = "createdAt")
    EventDto toDto(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "intervals", ignore = true)
    Event toEntity(EventDto eventDto);

}
