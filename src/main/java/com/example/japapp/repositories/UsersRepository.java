package com.example.japapp.repositories;

import com.example.japapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findById(long id);
}
