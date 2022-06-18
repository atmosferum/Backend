package ru.whattime.whattime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.whattime.whattime.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
