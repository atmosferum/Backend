package ru.whattime.whattime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
        Event event = eventRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with this uuid not found"));
        return eventMapper.toDto(event);
    }
}
