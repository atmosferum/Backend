package ru.whattime.whattime.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.whattime.whattime.auth.AuthTokenProvider;
import ru.whattime.whattime.dto.UserDto;
import ru.whattime.whattime.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final UserService service;
    private final AuthTokenProvider tokenProvider;

    private final Gson gson = new Gson();

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

    @GetMapping(path = "/currentUser")
    public ResponseEntity<?> currentUser() {
        return ResponseEntity.ok(gson.toJson(service.getCurrentUser()));
    }

    private int daysToSeconds(int days) {
        return days * 24 * 60 * 60;
    }
}
