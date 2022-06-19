package ru.whattime.whattime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.whattime.whattime.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
