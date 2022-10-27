package ru.meettime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.meettime.dto.UserDto;
import ru.meettime.mapper.UserMapper;
import ru.meettime.model.User;
import ru.meettime.repository.UserRepository;
import ru.meettime.security.SecurityContext;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityContext securityContext;

    public UserDto getCurrentUser() {
        User user = securityContext.getIdentified();
        return userMapper.toDto(user);
    }

    public boolean identifyUser(UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(userDto.getId());

        if (optionalUser.isPresent()) {
            securityContext.setIdentified(optionalUser.get());
            return true;
        }

        return false;
    }

    @Transactional
    public UserDto login(UserDto userDto) {
        if (securityContext.getIdentified() != null) {
            return renameUser(securityContext.getIdentified().getId(), userDto.getName());
        }
        return createUser(userDto.getName());
    }

    private UserDto createUser(String name) {
        User user = new User();
        user.setName(name);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    private UserDto renameUser(Long id, String name) {
        User user = userRepository.findById(id)
                .orElseThrow(IllegalStateException::new);
        user.setName(name);
        return userMapper.toDto(user);
    }
}
