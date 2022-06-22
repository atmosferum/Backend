package ru.whattime.whattime.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.UUID;

public class UUIDToStringConverter extends StdConverter<UUID, String> {

    @Override
    public String convert(UUID value) {
        return value.toString();
    }
}
