package ru.whattime.whattime.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.UUID;

public class UuidSerializer extends StdSerializer<UUID> {

    public UuidSerializer() {
        this(null);
    }

    public UuidSerializer(Class<UUID> t) {
        super(t);
    }

    @Override
    public void serialize(UUID uuid, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(uuid.toString());
    }
}
