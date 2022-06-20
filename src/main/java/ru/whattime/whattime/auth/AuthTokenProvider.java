package ru.whattime.whattime.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.whattime.whattime.dto.UserDTO;
import ru.whattime.whattime.encoder.Base64Encoder;
import ru.whattime.whattime.model.User;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AuthTokenProvider {

    private final Base64Encoder encoder;

    private final ObjectMapper mapper;

    public String provideToken(User user) throws JsonProcessingException {

        return encoder.encode(mapper.writeValueAsString(user));
    }

    public UserDTO parseToken(String token) {
        if (token == null) {
            return null;
        }

        Base64.Decoder decoder = Base64.getUrlDecoder();

        try {
            return mapper.readValue(decoder.decode(token), UserDTO.class);
        } catch (IOException e) {
            return null;
        }
    }


}
