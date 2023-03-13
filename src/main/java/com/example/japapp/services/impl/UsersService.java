package com.example.japapp.services.impl;

import com.example.japapp.dto.UserDto;
import com.example.japapp.models.Book;
import com.example.japapp.models.User;
import com.example.japapp.exeptions.MainException;
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
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object userAttribute = session.getAttribute("user");
            if (userAttribute != null && userAttribute instanceof User) {
                return true;
            }
        }
        return false;
    }

    public UserDto saveUser(User user) {
        if (usersRepository.findByEmail(user.getEmail()) != null) {
            throw new MainException("User with this email already exists!");
        }
        try {
            String encodedPassword = this.passwordService.hashPassword(user.getPassword());
            user.setPassword(encodedPassword);
            User savedUser = usersRepository.save(user);
            return new UserDto(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
        } catch (DataAccessException e) {
            throw new MainException("Unable to register user. Please try again later.");
        }
    }

    public UserDto authenticate(String email, String password) throws MainException {
        User user = usersRepository.findByEmail(email);

        if (user == null) {
            throw new MainException("Invalid email or password");
        }

        if (!this.passwordService.checkPassword(password, user.getPassword())) {
            throw new MainException("Invalid email or password");
        }

        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail());

        return userDto;
    }

    public UserDto findByEmail(String email) {
        User user = this.usersRepository.findByEmail(email);
        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        return userDto;
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
