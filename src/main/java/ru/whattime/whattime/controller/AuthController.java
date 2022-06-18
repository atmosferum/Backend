package ru.whattime.whattime.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.whattime.whattime.auth.AuthTokenProvider;
import ru.whattime.whattime.dto.UserDTO;
import ru.whattime.whattime.model.User;
import ru.whattime.whattime.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/api/v1/login")
@RequiredArgsConstructor
public class AuthController {
    @Value("${application.auth.cookie.name}")
    private String cookieName;

    @Value("${application.auth.cookie.maxAgeInDays}")
    private Integer maxAgeInDays;

    private final UserService service;

    private final AuthTokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<?> login(@Validated @RequestBody UserDTO userDTO, HttpServletResponse response) throws JsonProcessingException {

        User savedUser = service.registerUser(userDTO);

        Cookie cookie = new Cookie(cookieName, tokenProvider.provideToken(savedUser));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(daysToSeconds(maxAgeInDays));

        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }


    private int daysToSeconds(int days) {
        return days * 24 * 60 * 60;
    }
}
