package ru.whattime.whattime.encoder;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import ru.whattime.whattime.model.User;

@Component
public class Base64Encoder implements Encoder{

    @Override
    public String encodeUser(User user) {
        return Jwts.builder()
                .claim("name", user.getName())
                .claim("id", user.getId())
                .compact();
    }
}
