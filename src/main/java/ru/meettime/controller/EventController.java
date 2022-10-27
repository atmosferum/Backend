package ru.meettime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.meettime.dto.EventDto;
import ru.meettime.dto.IntervalDto;
import ru.meettime.service.EventService;
import ru.meettime.validation.NotOverlay;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/events")
@Validated
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createEvent(@RequestBody @Valid EventDto eventDto) {
        EventDto event = service.createEvent(eventDto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(event.getUuid())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PostMapping(path = "/{eventId}/intervals", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> putIntervals(@PathVariable UUID eventId,
                                          @RequestBody @NotOverlay List<@Valid IntervalDto> intervals) {
        service.putIntervals(intervals, eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{eventId}/intervals", produces = "application/json")
    public ResponseEntity<?> getIntervals(@PathVariable UUID eventId) {
        return ResponseEntity.ok(service.getIntervals(eventId));
    }

    @GetMapping(path = "/{eventId}/participants", produces = "application/json")
    public ResponseEntity<?> getParticipants(@PathVariable UUID eventId) {
        return ResponseEntity.ok(service.getParticipants(eventId));
    }

    @GetMapping(path = "/{eventId}/result", produces = "application/json")
    public ResponseEntity<?> getVotingResult(@PathVariable UUID eventId) {
        return ResponseEntity.ok(service.getVotingResult(eventId));
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> getEvent(@PathVariable UUID id) {
        EventDto eventDto = service.getEvent(id);
        if (eventDto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(eventDto);
    }
}