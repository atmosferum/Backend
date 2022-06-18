package ru.whattime.whattime.encoder;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import ru.whattime.whattime.model.User;

import java.util.Base64;

@Component
public class Base64Encoder {

    public String encode(String input) {
        return Base64.getUrlEncoder().encodeToString(input.getBytes());
    }
}
