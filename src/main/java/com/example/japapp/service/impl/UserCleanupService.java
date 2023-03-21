package com.example.japapp.service.impl;

import com.example.japapp.model.User;
import com.example.japapp.repository.UsersRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserCleanupService {
    private final UsersRepository usersRepository;

    public UserCleanupService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Scheduled(fixedRate = 86400000) // запускать задачу каждые 24 часа
    public void deleteInactiveUsers() {
        List<User> inactiveUsers = usersRepository.findByActiveFalseAndCreated_atBefore(LocalDateTime.now().minusMinutes(30));
        usersRepository.deleteAll(inactiveUsers);
    }
}
