package com.example.japapp.mapper;

import com.example.japapp.dto.UserDto;
import com.example.japapp.model.User;

public class UserMapper {
    private UserMapper() {}
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRoles());
    }
}
