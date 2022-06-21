package ru.whattime.whattime.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.whattime.whattime.dto.EventDTO;
import ru.whattime.whattime.encoder.Base64Encoder;
import ru.whattime.whattime.mapper.EventMapper;
import ru.whattime.whattime.mapper.UserMapper;
import ru.whattime.whattime.model.Event;
import ru.whattime.whattime.model.User;
import ru.whattime.whattime.repository.EventRepository;
import ru.whattime.whattime.repository.UserRepository;
import ru.whattime.whattime.service.EventService;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    EventMapper eventMapper;

    @MockBean
    UserRepository userRepository;

    @MockBean
    EventService eventService;

    @MockBean
    EventRepository eventRepository;

    @Autowired
    Base64Encoder encoder;

    @Value("${application.auth.cookie.name}")
    private String cookieName;

    private final User user = new User(1L, "John", new ArrayList<>());


    @BeforeEach
    @SneakyThrows
    void registerUser() {
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.getReferenceById(user.getId())).thenReturn(user);
    }

    @Test
    @SneakyThrows
    void createEventTest() {
        EventDTO eventDto = EventDTO.builder()
                .title("title")
                .description("description")
                .owner(userMapper.toDTO(user))
                .build();

        UUID uuid = UUID.randomUUID();

        Event event = eventMapper.toEntity(eventDto);
        event.setUuid(uuid);

        Mockito.when(eventService.createEvent(eventDto)).thenReturn(event);

        Cookie cookie = new Cookie(cookieName, encoder.encode(objectMapper.writeValueAsString(user)));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/event")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .cookie(cookie)
                .content(this.objectMapper.writeValueAsString(eventDto));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://*/api/v1/event/" + uuid));
    }
}