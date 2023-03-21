package com.example.japapp.repository;

import com.example.japapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Optional<User> findById(long id);
    Optional<User> findByVerificationToken(String token);
    @Modifying
    @Query("DELETE FROM users u WHERE u.active = false AND u.created_at < :date")
    void deleteInactiveUsers(@Param("date") LocalDateTime date);
}
