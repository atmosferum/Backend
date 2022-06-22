package ru.whattime.whattime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.whattime.whattime.dto.EventDto;
import ru.whattime.whattime.dto.IntervalDto;
import ru.whattime.whattime.dto.UserDto;
import ru.whattime.whattime.dto.VoitingResultDto;
import ru.whattime.whattime.mapper.EventMapper;
import ru.whattime.whattime.mapper.IntervalMapper;
import ru.whattime.whattime.mapper.UserMapper;
import ru.whattime.whattime.model.Event;
import ru.whattime.whattime.model.Interval;
import ru.whattime.whattime.model.User;
import ru.whattime.whattime.repository.EventRepository;
import ru.whattime.whattime.repository.IntervalRepository;
import ru.whattime.whattime.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.*;
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
    private final UserMapper userMapper;

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
    public void putIntervals(List<IntervalDto> intervalDtos, UUID eventId) {
        Event event = eventRepository.findByUuid(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The event does not exist."));

        User user = userRepository.getReferenceById(userService.getCurrentUser().getId());

        // TODO: Разобраться с этой бесовщиной.
        event.getIntervals().removeIf(i -> i.getOwner().getId().equals(user.getId()));
        eventRepository.save(event);

        List<Interval> intervals = intervalDtos.stream().map(intervalDto -> {
            Interval interval = intervalMapper.toEntity(intervalDto);
            interval.setEvent(event);
            interval.setOwner(user);

            return interval;
        }).collect(Collectors.toList());

        intervalRepository.saveAll(intervals);
    }


    @Transactional
    public List<IntervalDto> getIntervals(UUID eventId) {
        Event event = eventRepository.findByUuid(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The event does not exist."));

        return event.getIntervals().stream().map(intervalMapper::toDto).toList();
    }

    @Transactional
    public VoitingResultDto getVoitingResult(UUID eventId) {

        record IntervalPart(Long time, boolean start) {
        }

        Event event = eventRepository.findByUuid(eventId)
                .orElseThrow(IllegalArgumentException::new);

        var intervals = event.getIntervals().stream()
                .map(intervalMapper::toDto)
                .toList();

        List<IntervalPart> parts = new ArrayList<>();
        Set<UserDto> participants = new HashSet<>();

        for (var i : intervals) {
            parts.add(new IntervalPart(i.getStartTime(), true));
            parts.add(new IntervalPart(i.getEndTime(), false));
            participants.add(i.getOwner());
        }

        parts.sort(
                Comparator.comparing(IntervalPart::time)
                        .thenComparing(IntervalPart::start)
        );

        List<IntervalDto> result = new ArrayList<>();

        int votes = 0;
        for (int i = 0; i < parts.size() - 1; i++) {
            votes = parts.get(i).start ? votes - 1 : votes + 1;
            if (votes == participants.size()) {
                result.add(new IntervalDto(parts.get(i).time, parts.get(i + 1).time));
            }
        }

        return VoitingResultDto.builder()
                .event(eventMapper.toDto(event))
                .intervals(result)
                .participants(new ArrayList<>(participants))
                .build();
    }
}
