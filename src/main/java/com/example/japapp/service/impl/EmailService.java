package com.example.japapp.service.impl;

import com.example.japapp.exception.MainException;
import com.example.japapp.model.User;
import com.example.japapp.repository.UsersRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UsersRepository usersRepository;
    private final TemplateEngine templateEngine;
    private final HttpServletRequest request;

    public EmailService(JavaMailSender javaMailSender,
                        UsersRepository usersRepository,
                        TemplateEngine templateEngine,
                        HttpServletRequest request) {
        this.javaMailSender = javaMailSender;
        this.usersRepository = usersRepository;
        this.templateEngine = templateEngine;
        this.request = request;
    }


    public void register(User user) throws MessagingException {
        String token = UUID.randomUUID().toString();

        user.setVerificationToken(token);
        user.setActive(false);
        usersRepository.save(user);

        this.sendVerificationEmail(user.getEmail(), token);
    }

    public void sendVerificationEmail(String email, String token) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject("Подтверждение регистрации");

        String domain = request.getServerName();
        int port = request.getServerPort();

        Context context = new Context();
        context.setVariable("domain", domain + ":" + port);
        context.setVariable("token", token);

        String html = templateEngine.process("confirm-email", context);
        helper.setText(html, true);

        javaMailSender.send(message);
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