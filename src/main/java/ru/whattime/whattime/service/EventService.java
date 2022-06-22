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


    @Transactional
    public List<IntervalDto> getIntervals(UUID eventId) {
        Event event = eventRepository.findByUuid(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The event does not exist."));

        return event.getIntervals().stream().map(intervalMapper::toDto).toList();
    }

    @Transactional
    public VoitingResultDto getVoitingResult(UUID eventId) {
        Event event = eventRepository.findByUuid(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The event does not exist."));

        VoitingResultDto.VoitingResultDtoBuilder voitingResult = VoitingResultDto.builder()
                .event(eventMapper.toDto(event));

        Set<UserDto> participants = new HashSet<>();

        List<Interval> intervals = event.getIntervals();
        List<Interval> ownerIntervals = intervals.stream().filter(interval -> interval.getOwner().equals(event.getOwner())).toList();
        List<Interval> participantsIntervals = intervals.stream().filter(interval -> !interval.getOwner().equals(event.getOwner())).toList();

        participantsIntervals.forEach(interval -> participants.add(userMapper.toDto(interval.getOwner())));

        int maxVoices = 0;
        List<Interval> candidateIntervals = new ArrayList<>();

        for (Interval ownerInterval : ownerIntervals) {
            Interval candidateInterval = new Interval();
            candidateInterval.setStartTime(ownerInterval.getStartTime());
            candidateInterval.setEndTime(ownerInterval.getEndTime());
            int voices = 0;

            for (Interval participantsInterval : participantsIntervals) {
                if ((participantsInterval.getStartTime().isAfter(ownerInterval.getStartTime()) || participantsInterval.getStartTime().isEqual(ownerInterval.getStartTime()))
                        && (participantsInterval.getEndTime().isBefore(ownerInterval.getEndTime()) || participantsInterval.getEndTime().isEqual(ownerInterval.getEndTime()))) {
                    if (candidateInterval.getStartTime().isBefore(participantsInterval.getStartTime())) {
                        candidateInterval.setStartTime(participantsInterval.getStartTime());
                    }

                    if (candidateInterval.getEndTime().isAfter(participantsInterval.getEndTime())) {
                        candidateInterval.setEndTime(participantsInterval.getEndTime());
                    }

                    voices++;
                }
            }

            if (voices > maxVoices) {
                maxVoices = voices;
                candidateIntervals.clear();
                candidateIntervals.add(candidateInterval);
            } else if(voices == maxVoices) {
                candidateIntervals.add(candidateInterval);
            }
        }

        voitingResult.intervals(candidateIntervals.stream().map(intervalMapper::toDto).toList());
        voitingResult.participants(participants);

        return voitingResult.build();

    }
}
