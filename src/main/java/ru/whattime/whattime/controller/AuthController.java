package ru.whattime.whattime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.whattime.whattime.auth.AuthTokenProvider;
import ru.whattime.whattime.dto.UserDto;
import ru.whattime.whattime.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
public class AuthController {
    @Value("${application.auth.cookie.name}")
    private String cookieName;

    @Value("${application.auth.cookie.maxAgeInDays}")
    private Integer maxAgeInDays;

    @Value("${application.auth.cookie.empty}")
    private String emptyCookie;

    private final UserService service;

    private final AuthTokenProvider tokenProvider;

    @PostMapping(path = "/login", consumes = "application/json")
    public ResponseEntity<?> login(@Validated @RequestBody UserDTO userDTO, HttpServletResponse response) throws JsonProcessingException {
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

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, emptyCookie);
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
