package ru.whattime.whattime.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.whattime.whattime.converter.LocalDateTimeToLongConverter;
import ru.whattime.whattime.converter.LongToLocalDateTimeConverter;
import ru.whattime.whattime.dto.IntervalDto;
import ru.whattime.whattime.model.Interval;

@Mapper(uses = {UserMapper.class, LocalDateTimeToLongConverter.class, LongToLocalDateTimeConverter.class})
public interface IntervalMapper {
    IntervalDto toDto(Interval event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "event", ignore = true)
    Interval toEntity(IntervalDto eventDto);
}
