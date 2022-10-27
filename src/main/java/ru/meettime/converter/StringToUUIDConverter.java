package ru.meettime.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.UUID;

public class StringToUUIDConverter extends StdConverter<String, UUID> {

    @Override
    public UUID convert(String value) {
        return UUID.fromString(value);
    }
}
