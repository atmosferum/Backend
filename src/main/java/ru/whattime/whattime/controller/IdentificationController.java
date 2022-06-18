package ru.whattime.whattime.controller;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.whattime.whattime.model.User;
import ru.whattime.whattime.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/api/v1/login")
@RequiredArgsConstructor
public class IdentificationController {
    @Value("${application.auth.cookie.name}")
    private String cookieName;

    @Value("${application.auth.cookie.maxAgeInDays}")
    private Integer maxAgeInDays;

    private final UserService service;

    @PostMapping("/test")
    public ResponseEntity<?> test() {
        return  ResponseEntity.ok("SDfsdf");
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {

        if (user.getName() == null || user.getName().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid user name");
        }

        User savedUser = service.addUser(user);

        Cookie cookie = new Cookie(cookieName, getTokenByUser(savedUser));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(daysToSeconds(maxAgeInDays));

        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }

    private String getTokenByUser(User user) {
        return Jwts.builder()
                .claim("name", user.getName())
                .claim("id", user.getId())
                .compact();
    }

    private int daysToSeconds(int days) {
        return days * 24 * 60 * 60;
    }
}
