package ru.whattime.whattime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.whattime.whattime.dto.UserDTO;
import ru.whattime.whattime.mapper.UserMapper;
import ru.whattime.whattime.model.User;
import ru.whattime.whattime.repository.UserRepository;
import ru.whattime.whattime.security.SecurityContext;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    private final UserMapper mapper;

    private final SecurityContext securityContext;

    public UserDTO getCurrentUser() {
        User user = securityContext.getIdentified();
        return mapper.toDTO(user);
    }

    public void identifyUser(UserDTO userDto) {
        securityContext.setIdentified(mapper.toEntity(userDto));
    }

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
