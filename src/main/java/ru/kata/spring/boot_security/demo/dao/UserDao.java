package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    void addUser(User user);

    void updateUser(User user);

    void deleteUser(int id);

    List<User> getAllUsers();

    User getUserById(int id);

    User getUserByEmail(String email);
}
