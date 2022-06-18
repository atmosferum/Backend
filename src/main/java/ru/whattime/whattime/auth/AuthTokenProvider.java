package ru.whattime.whattime.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.whattime.whattime.encoder.Base64Encoder;
import ru.whattime.whattime.model.User;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AuthTokenProvider {

    private final Base64Encoder encoder;

    public String provideToken(User user) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return encoder.encode(mapper.writeValueAsString(user));
    }

    public User parseToken(String token) throws IOException {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(decoder.decode(token), User.class);
    }


}
