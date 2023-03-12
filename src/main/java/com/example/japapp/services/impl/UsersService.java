package com.example.japapp.services.impl;

import com.example.japapp.models.Book;
import com.example.japapp.models.User;
import com.example.japapp.exeptions.AuthenticationException;
import com.example.japapp.exeptions.RegistrationException;
import com.example.japapp.repositories.UsersRepository;
import com.example.japapp.services.PasswordService;
import org.hibernate.Hibernate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordService passwordService;

    public UsersService(UsersRepository usersRepository, PasswordService passwordService) {
        this.usersRepository = usersRepository;
        this.passwordService = passwordService;
    }

    public boolean isUserAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // получаем текущую сессию, но не создаем новую, если ее нет
        if (session != null) {
            Object userAttribute = session.getAttribute("user");
            if (userAttribute != null && userAttribute instanceof User) {
                // если атрибут "user" есть в сессии и является объектом User, значит пользователь авторизован
                return true;
            }
        }
        return false;
    }

    public User saveUser(User user) {
        if (findByEmail(user.getEmail()) != null) {
            throw new RegistrationException("User with this email already exists!");
        }
        try {
            String encodedPassword = this.passwordService.hashPassword(user.getPassword());
            user.setPassword(encodedPassword);
            return usersRepository.save(user);
        } catch (DataAccessException e) {
            throw new RegistrationException("Unable to register user. Please try again later.");
        }
    }

    public User authenticate(String email, String password) throws AuthenticationException {
        User user = this.findByEmail(email);

        if (user == null) {
            throw new AuthenticationException("Invalid email or password");
        }

        if (!this.passwordService.checkPassword(password, user.getPassword())) {
            throw new AuthenticationException("Invalid email or password");
        }

        return user;
    }

    public User findByEmail(String email) {
        return this.usersRepository.findByEmail(email);
    }

    @Transactional
    public List<Book> findAllBooksByUserId(Long userId) {
        User user = usersRepository.findById(userId).orElse(null);
        if (user != null) {
            Hibernate.initialize(user.getBooks());
            return user.getBooks();
        }
        return null;
    }
}
