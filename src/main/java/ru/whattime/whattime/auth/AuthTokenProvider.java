package ru.whattime.whattime.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.whattime.whattime.encoder.Base64Encoder;
import ru.whattime.whattime.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AuthTokenProvider {

    private final Base64Encoder encoder;

    public String provideToken(User user) {
        return encoder.encodeUser(user);
    }

    public User parseToken(String token) throws IOException {
        String[] parts = token.split("\\.");
        String body = parts[1];

        Base64.Decoder decoder = Base64.getUrlDecoder();
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(decoder.decode(body), User.class);
    }


}
