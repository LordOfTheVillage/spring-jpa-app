package com.example.japapp.service.impl;

import com.example.japapp.exception.MainException;
import com.example.japapp.model.User;
import com.example.japapp.repository.UsersRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;
    private UsersRepository usersRepository;

    public EmailService(JavaMailSender javaMailSender, UsersRepository usersRepository) {
        this.javaMailSender = javaMailSender;
        this.usersRepository = usersRepository;
    }


    public void register(User user) {
        // отправляем электронное письмо с ссылкой на подтверждение регистрации
        String token = UUID.randomUUID().toString();

        user.setVerificationToken(token);
        user.setActive(false);
        usersRepository.save(user);

        this.sendVerificationEmail(user, token);
    }

    public void sendVerificationEmail(User user, String token) {
        String confirmationUrl = "http://localhost:8080/confirm?token=" + token;
        String message = "Здравствуйте, " + user.getEmail() + "! Для подтверждения регистрации на сайте перейдите по ссылке: " + confirmationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Подтверждение регистрации");
        email.setText(message);
        javaMailSender.send(email);
    }


    public User confirm(String token) {
        User user = usersRepository.findByVerificationToken(token)
                .orElseThrow(() -> new MainException("User not found for token: " + token));
        user.setActive(true);
        user.setVerificationToken(null);
        usersRepository.save(user);
        return user;
    }
}