package ru.whattime.whattime.mapper;

import org.mapstruct.Mapper;
import ru.whattime.whattime.dto.UserDTO;
import ru.whattime.whattime.model.User;

import java.util.List;

@Mapper
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
}
