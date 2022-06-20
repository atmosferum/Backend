package ru.whattime.whattime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.whattime.whattime.auth.AuthTokenProvider;
import ru.whattime.whattime.dto.EventDTO;
import ru.whattime.whattime.model.Event;
import ru.whattime.whattime.service.EventService;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("api/v1/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    private final AuthTokenProvider tokenProvider;


    @PostMapping
    public ResponseEntity<?> createEvent(@Validated @RequestBody EventDTO eventDto) throws URISyntaxException {
        Event event = service.createEvent(eventDto);

        return ResponseEntity.created(new URI("http://localhost:8080/event/" + event.getUuid())).build();
    }


}
