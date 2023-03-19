package com.example.japapp.repository;

import com.example.japapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Optional<User> findById(long id);
}
