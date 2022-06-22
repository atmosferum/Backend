package ru.whattime.whattime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.whattime.whattime.model.Event;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByUuid(UUID uuid);
}
