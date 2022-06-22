package ru.whattime.whattime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.whattime.whattime.dto.EventDto;
import ru.whattime.whattime.mapper.EventMapper;
import ru.whattime.whattime.model.Event;
import ru.whattime.whattime.model.User;
import ru.whattime.whattime.repository.EventRepository;
import ru.whattime.whattime.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final UserService userService;

    private final Pattern UUID_REGEX_PATTERN = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

    @Transactional
    public EventDto createEvent(EventDto eventDto) {
        Event event = eventMapper.toEntity(eventDto);
        event.setUuid(UUID.randomUUID());

        User user = userRepository.getReferenceById(userService.getCurrentUser().getId());

        event.setOwner(user);
        eventRepository.save(event);

        return eventMapper.toDto(event);
    }

    public EventDto getEvent(UUID uuid) {
        Optional<Event> event = eventRepository.findByUuid(uuid);
        if (event.isEmpty()) return null;
        return eventMapper.toDto(event.get());
    }

    // TODO Подумать
    public Boolean isUuid(String uuid) {
        if (uuid == null) {
            return false;
        }
        return UUID_REGEX_PATTERN.matcher(uuid).matches();
    }
}
