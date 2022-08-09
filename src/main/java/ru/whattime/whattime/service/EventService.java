package ru.whattime.whattime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.whattime.whattime.dto.*;
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
    public List<UserDto> getParticipants(UUID eventId) {
        Event event = eventRepository.findByUuid(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The event does not exist."));

        List<Interval> intervals = event.getIntervals();
        intervals.sort(Comparator.comparing(Interval::getCreated));

        List<IntervalDto> intervalDtoList = intervals.stream()
                .map(intervalMapper::toDto)
                .toList();

        Set<UserDto> participants = new HashSet<>();

        intervalDtoList.forEach(interval -> participants.add(interval.getOwner()));

        return participants.stream().toList();
    }

    @Transactional
    public VotingResultDto getVotingResult(UUID eventId) {
        record IntervalPart(UserDto owner, Long time, boolean start) {}

        Event event = eventRepository.findByUuid(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The event does not exist."));

        List<IntervalDto> intervals = event.getIntervals().stream()
                .map(intervalMapper::toDto)
                .toList();

        List<IntervalPart> parts = new ArrayList<>();

        for (IntervalDto i : intervals) {
            parts.add(new IntervalPart(i.getOwner(), i.getStartTime(), true));
            parts.add(new IntervalPart(i.getOwner(), i.getEndTime(), false));
        }

        parts.sort(Comparator.comparing(IntervalPart::time)
                        .thenComparing(IntervalPart::start));

        List<ResultIntervalDto> resultIntervals = new ArrayList<>();
        Set<UserDto> intervalParticipants = new HashSet<>();

        for (int i = 0; i < parts.size(); i++) {

            if (parts.get(i).start) {
                intervalParticipants.add(parts.get(i).owner);
            } else {
                resultIntervals.add(ResultIntervalDto.builder()
                        .startTime(parts.get(i - 1).time)
                        .endTime(parts.get(i).time)
                        .owners(intervalParticipants.stream().toList())
                        .build());
                intervalParticipants.remove(parts.get(i).owner);
            }
        }

        ResultIntervalDto max = resultIntervals.stream().max(Comparator.comparing(resultInterval -> resultInterval.getOwners().size())).get();
        resultIntervals = resultIntervals.stream().filter(resultInterval -> resultInterval.getOwners().size() == max.getOwners().size()).collect(Collectors.toList());

        return VotingResultDto.builder()
                .event(eventMapper.toDto(event))
                .intervals(resultIntervals)
                .build();
    }
}
