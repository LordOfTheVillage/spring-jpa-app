package com.example.japapp.service.impl;

import com.example.japapp.repository.UsersRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class UserCleanupTask extends TimerTask {

    private final UsersRepository userRepository;

    public UserCleanupTask(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run() {
        LocalDateTime date = LocalDateTime.now().minusMinutes(30);
        userRepository.deleteInactiveUsers(date);
    }

    @PostConstruct
    public void startTimer() {
        Timer timer = new Timer();
        timer.schedule(this, 0, 30 * 60 * 1000);
    }
}