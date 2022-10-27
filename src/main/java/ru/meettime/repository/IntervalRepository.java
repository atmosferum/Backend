package ru.meettime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.meettime.model.Interval;

public interface IntervalRepository extends JpaRepository<Interval, Long> {
}
