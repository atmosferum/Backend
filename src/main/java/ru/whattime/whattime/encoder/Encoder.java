package ru.whattime.whattime.encoder;

import ru.whattime.whattime.model.User;

public interface Encoder {
    String encodeUser(User user);
}
