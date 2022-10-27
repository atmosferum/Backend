package ru.meettime.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.meettime.dto.IntervalDto;
import ru.meettime.model.Interval;

@Mapper(uses = {UserMapper.class, LocalDateTimeMapper.class})
public interface IntervalMapper {
    IntervalDto toDto(Interval event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "event", ignore = true)
    Interval toEntity(IntervalDto eventDto);
}
