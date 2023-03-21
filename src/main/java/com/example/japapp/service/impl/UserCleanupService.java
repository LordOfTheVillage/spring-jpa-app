package com.example.japapp.service.impl;

import com.example.japapp.model.User;
import com.example.japapp.repository.UsersRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserCleanupService {
    private final UsersRepository usersRepository;

    public UserCleanupService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Scheduled(fixedRate = 1800000)
    @Transactional
    public void deleteInactiveUsers() {
        LocalDateTime date = LocalDateTime.now().minusMinutes(30);
        usersRepository.deleteInactiveUsers(date);
    }
}
