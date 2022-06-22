package ru.whattime.whattime.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.whattime.whattime.dto.EventDto;
import ru.whattime.whattime.service.EventService;
import ru.whattime.whattime.validation.IsUuid;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventDto eventDto) {
        EventDto event = service.createEvent(eventDto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(event.getUuid())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> getEvent(@IsUuid @PathVariable String id) {
        EventDto eventDto = service.getEvent(UUID.fromString(id));
        if (eventDto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(eventDto);
    }
}