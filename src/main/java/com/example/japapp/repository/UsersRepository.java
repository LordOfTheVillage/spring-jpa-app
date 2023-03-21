package com.example.japapp.repository;

import com.example.japapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Optional<User> findById(long id);
    Optional<User> findByVerificationToken(String token);
    List<User> findByActiveFalseAndCreated_atBefore(LocalDateTime dateTime);
}
