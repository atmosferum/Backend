package ru.meettime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.meettime.auth.AuthTokenProvider;
import ru.meettime.dto.UserDto;
import ru.meettime.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final UserService service;
    private final AuthTokenProvider tokenProvider;

    @Value("${application.auth.cookie.name}")
    private String cookieName;

    @Value("${application.auth.cookie.maxAgeInDays}")
    private Integer maxAgeInDays;

    @PostMapping(path = "/login", consumes = "application/json")
    public ResponseEntity<?> login(@Valid @RequestBody UserDto userDTO, HttpServletResponse response) {
        UserDto loggedIn = service.login(userDTO);
        String authToken = tokenProvider.provideToken(loggedIn)
                .orElseThrow(IllegalStateException::new);

        Cookie cookie = new Cookie(cookieName, authToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(daysToSeconds(maxAgeInDays));

        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/currentUser", produces = "application/json")
    public ResponseEntity<?> currentUser() {
        return ResponseEntity.ok(service.getCurrentUser());
    }

    private int daysToSeconds(int days) {
        return days * 24 * 60 * 60;
    }
}
