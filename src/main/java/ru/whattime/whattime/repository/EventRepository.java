package ru.whattime.whattime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.whattime.whattime.model.Event;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByUuid(UUID uuid);
}
