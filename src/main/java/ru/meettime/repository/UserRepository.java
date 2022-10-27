package ru.meettime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.meettime.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
