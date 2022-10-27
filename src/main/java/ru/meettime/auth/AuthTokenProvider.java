package ru.meettime.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;
import ru.meettime.dto.UserDto;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthTokenProvider {

    private final ObjectMapper objectMapper;

    public Optional<String> provideToken(UserDto data) {
        if (data == null) {
            return Optional.empty();
        }

        String encoded;
        try {
            encoded = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }

        return Optional.ofNullable(Base64.encodeBase64String(encoded.getBytes()));
    }

    public Optional<UserDto> parseToken(String data) {
        String decoded = new String(Base64.decodeBase64(data));
        try {
            return Optional.ofNullable(objectMapper.readValue(decoded, UserDto.class));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}