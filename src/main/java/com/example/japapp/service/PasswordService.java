package com.example.japapp.service;

public interface PasswordService {
    String hashPassword(String password);
    boolean checkPassword(String password, String passwordHash);
}
