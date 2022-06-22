package ru.whattime.whattime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.whattime.whattime.model.Interval;

public interface IntervalRepository extends JpaRepository<Interval, Long> {
}
