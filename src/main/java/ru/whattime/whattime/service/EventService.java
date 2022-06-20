package ru.whattime.whattime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.whattime.whattime.dto.EventDTO;
import ru.whattime.whattime.mapper.EventMapper;
import ru.whattime.whattime.model.Event;
import ru.whattime.whattime.repository.EventRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository repository;

    private final EventMapper mapper;

    private final UserService userService;

    public Event createEvent(EventDTO eventDto) {
        eventDto.setUuid(UUID.randomUUID().toString());
        eventDto.setOwner(userService.getCurrentUser());


        return repository.save(mapper.toEntity(eventDto));
    }
}
