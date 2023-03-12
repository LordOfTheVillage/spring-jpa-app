package com.example.japapp.services;

public interface PasswordService {
    String hashPassword(String password);
    boolean checkPassword(String password, String passwordHash);
}
