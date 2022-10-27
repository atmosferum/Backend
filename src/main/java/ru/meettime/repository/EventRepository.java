package ru.meettime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.meettime.model.Event;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByUuid(UUID uuid);
}
