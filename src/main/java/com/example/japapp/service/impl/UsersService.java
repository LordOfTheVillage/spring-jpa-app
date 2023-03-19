package com.example.japapp.service.impl;

import com.example.japapp.dto.UserDto;
import com.example.japapp.mapper.UserMapper;
import com.example.japapp.model.Book;
import com.example.japapp.model.User;
import com.example.japapp.exception.MainException;
import com.example.japapp.repository.UsersRepository;
import com.example.japapp.service.PasswordService;
import org.hibernate.Hibernate;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordService passwordService;
    private final RolesService rolesService;

    public UsersService(UsersRepository usersRepository, PasswordService passwordService, RolesService rolesService) {
        this.usersRepository = usersRepository;
        this.passwordService = passwordService;
        this.rolesService = rolesService;
    }

    public boolean isUserAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object userAttribute = session.getAttribute("user");
            // TODO: Logic of authenticate
            return userAttribute instanceof User;
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
            rolesService.setUserRole(user);
            User savedUser = usersRepository.save(user);
            return UserMapper.toUserDto(savedUser);
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

        return UserMapper.toUserDto(user);
    }

    public UserDto findByEmail(String email) {
        User user = this.usersRepository.findByEmail(email);
        return UserMapper.toUserDto(user);
    }

    public List<UserDto> findAllUsers() {
        List<User> users = this.usersRepository.findAll();
        return users.stream().map(UserMapper::toUserDto).toList();
    }

    public UserDto setAdminRole(Long id) {
        Optional<User> optionalUser = usersRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new MainException("Unable to change user's role. Please try again later.");
        }

        User user = optionalUser.get();
        rolesService.setAdminRole(user);
        User savedUser = usersRepository.save(user);

        return UserMapper.toUserDto(savedUser);
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
