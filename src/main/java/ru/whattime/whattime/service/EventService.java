package ru.whattime.whattime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.whattime.whattime.dto.EventDto;
import ru.whattime.whattime.dto.IntervalDto;
import ru.whattime.whattime.mapper.EventMapper;
import ru.whattime.whattime.mapper.IntervalMapper;
import ru.whattime.whattime.model.Event;
import ru.whattime.whattime.model.Interval;
import ru.whattime.whattime.model.User;
import ru.whattime.whattime.repository.EventRepository;
import ru.whattime.whattime.repository.IntervalRepository;
import ru.whattime.whattime.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final IntervalRepository intervalRepository;
    private final EventMapper eventMapper;
    private final IntervalMapper intervalMapper;
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

    @Transactional
    public void putIntervals(List<IntervalDto> intervalDtos, String eventId) {
        Event event = eventRepository.findEventByUuid(UUID.fromString(eventId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The event does not exist."));

        User user = userRepository.getReferenceById(userService.getCurrentUser().getId());

        event.getIntervals().removeIf(i -> i.getOwner().equals(user));
        eventRepository.save(event);

        List<Interval> intervals = intervalDtos.stream().map(intervalDto -> {
            Interval interval = intervalMapper.toEntity(intervalDto);
            interval.setEvent(event);
            interval.setOwner(user);

            return interval;
        }).collect(Collectors.toList());

        intervalRepository.saveAll(intervals);
    }

}
