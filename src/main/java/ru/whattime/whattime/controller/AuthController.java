package ru.whattime.whattime.controller;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.whattime.whattime.auth.AuthTokenProvider;
import ru.whattime.whattime.dto.UserDTO;
import ru.whattime.whattime.encoder.Base64Encoder;
import ru.whattime.whattime.mapper.UserMapper;
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

    private final UserMapper mapper;

    private final AuthTokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO, HttpServletResponse response) {

        if (userDTO.getName() == null || userDTO.getName().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid user name");
        }

        User user = mapper.toUser(userDTO);
        User savedUser = service.save(user);

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
