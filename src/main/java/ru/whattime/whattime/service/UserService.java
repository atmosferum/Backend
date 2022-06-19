package ru.whattime.whattime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.whattime.whattime.dto.UserDTO;
import ru.whattime.whattime.mapper.UserMapper;
import ru.whattime.whattime.model.User;
import ru.whattime.whattime.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    private final UserMapper mapper;

    public User registerUser(UserDTO userDto) {
        User user = mapper.toEntity(userDto);
        return repository.save(user);
    }

    public Optional<UserDTO> getUserById(Long id) {
        return repository
                .findById(id)
                .map(mapper::toDTO);
    }


}
