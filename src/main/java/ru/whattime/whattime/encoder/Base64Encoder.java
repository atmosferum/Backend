package ru.whattime.whattime.encoder;


import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class Base64Encoder {

    public String encode(String input) {
        return Base64.getUrlEncoder().encodeToString(input.getBytes());
    }
}
