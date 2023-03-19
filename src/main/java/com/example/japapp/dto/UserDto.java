package com.example.japapp.dto;

import com.example.japapp.model.Book;
import com.example.japapp.model.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDto {

    private Long id;

    private String name;

    private String email;

    private List<Book> books = new ArrayList<>();

    private Set<Role> roles = new HashSet<>();

    public UserDto() {}
    public UserDto(Long id, String name, String email, List<Book> books) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.books = books;
    }

    public UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UserDto(Long id, String name, String email, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", books=" + books +
                ", roles=" + roles +
                '}';
    }
}