package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    void addUser(User user, List<String> rolesList);

    void updateUser(User user, List<String> rolesList);

    void deleteUser(int id);

    List<User> getAllUsers();

    User getUserById(int id);

    User getUserByEmail(String email);
}

