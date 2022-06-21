package ru.whattime.whattime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.whattime.whattime.dto.EventDTO;
import ru.whattime.whattime.mapper.EventMapper;
import ru.whattime.whattime.model.Event;
import ru.whattime.whattime.model.User;
import ru.whattime.whattime.repository.EventRepository;
import ru.whattime.whattime.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final EventMapper eventMapper;

    private final UserService userService;

    public Event createEvent(EventDTO eventDto) {
        Event event = eventMapper.toEntity(eventDto);
        event.setUuid(UUID.randomUUID());

        User user = userRepository.getReferenceById(userService.getCurrentUser().getId());

        event.setOwner(user);

        user.getEvents().add(event);

        return eventRepository.save(event);
    }
}
