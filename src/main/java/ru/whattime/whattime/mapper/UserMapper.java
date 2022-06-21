package ru.whattime.whattime.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.whattime.whattime.dto.UserDto;
import ru.whattime.whattime.model.User;

@Mapper
public interface UserMapper {
    UserDto toDto(User user);

    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toEntity(UserDto userDTO);
}
