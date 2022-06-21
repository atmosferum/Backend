package ru.whattime.whattime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.whattime.whattime.dto.EventDto;
import ru.whattime.whattime.dto.IntervalDto;
import ru.whattime.whattime.service.EventService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/events")
@Validated
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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleException(ConstraintViolationException constraintViolationException) {
        Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
        String errorMessage = "";
        if (!violations.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            violations.forEach(violation -> builder.append(" " + violation.getMessage()));
            errorMessage = builder.toString();
        } else {
            errorMessage = "ConstraintViolationException occured.";
        }
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/{id}/intervals", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> putIntervals(@PathVariable String id, @RequestBody List<@Valid IntervalDto> intervalDtoList) {
        if (service.putIntervals(intervalDtoList, id)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}