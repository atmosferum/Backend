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
import ru.whattime.whattime.validator.IntervalsValidator;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    private final IntervalsValidator intervalsValidator;

    @Transactional
    public EventDto createEvent(EventDto eventDto) {
        Event event = eventMapper.toEntity(eventDto);
        event.setUuid(UUID.randomUUID());

        User user = userRepository.getReferenceById(userService.getCurrentUser().getId());

        event.setOwner(user);
        eventRepository.save(event);

        return eventMapper.toDto(event);
    }

    @Transactional
    public void putIntervals(List<IntervalDto> intervalDtoList, String eventId) {
        if (!intervalsValidator.validate(intervalDtoList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad intervals content");
        }

        Optional<Event> optionalEvent = eventRepository.findEventByUuid(UUID.fromString(eventId));

        if (optionalEvent.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not exists");
        }

        Event event = optionalEvent.get();

        User user = userRepository.getReferenceById(userService.getCurrentUser().getId());

        event.getIntervals().removeIf(i -> i.getOwner().equals(user));
        eventRepository.save(event);

        List<Interval> intervals = intervalDtoList.stream().map(intervalDto -> {
            Interval interval = intervalMapper.toEntity(intervalDto);
            interval.setEvent(event);
            interval.setOwner(user);

            return interval;
        }).collect(Collectors.toList());

        intervalRepository.saveAll(intervals);
    }

}
