package ru.meettime.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.meettime.dto.EventDto;
import ru.meettime.model.Event;

@Mapper(uses = {UserMapper.class, LocalDateTimeMapper.class})
public interface EventMapper {

    @Mapping(source = "created", target = "createdAt")
    EventDto toDto(Event event);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "intervals", ignore = true)
    @Mapping(target = "id", ignore = true)
    Event toEntity(EventDto eventDto);

}
