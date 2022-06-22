package ru.whattime.whattime.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.whattime.whattime.dto.EventDto;
import ru.whattime.whattime.service.EventService;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventDto eventDto) {
        EventDto event = service.createEvent(eventDto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(event.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable String id) {
        if (!isUuid(id))
            return ResponseEntity.notFound().build(); // Лучше бросать Bad request, но по open-api низя

        EventDto eventDto = service.getEventByUuid(UUID.fromString(id));
        if (eventDto == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(objectWriter.writeValueAsString(eventDto));
    }

    private Boolean isUuid(String uuid) {
        Pattern UUID_REGEX_PATTERN = Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

        if (uuid == null) {
            return false;
        }
        return UUID_REGEX_PATTERN.matcher(uuid).matches();
    }
}