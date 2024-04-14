package ru.kata.spring.boot_security.demo.dao;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    @Override
    public User getUserById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void addUser(User user) {
        if (getUserByEmail(user.getEmail()) == null) {
            if (getUserById(1) != null && getUserById(2) != null) {
                Set<Role> roles = getDistinctRoles(user.getRoles());
                user.setRoles(roles);
            }
            user.setPassword(encodePassword(user.getPassword()));
            entityManager.persist(user);
        }
    }

    @Override
    public void updateUser(User user) {
        User existingUser = getUserById(user.getId());
        if (existingUser != null) {
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setAge(user.getAge());
            existingUser.setEmail(getUserByEmail(user.getEmail()) != null ? existingUser.getEmail() : user.getEmail());
            existingUser.setPassword(user.getPassword().isEmpty() ? existingUser.getPassword() : encodePassword(user.getPassword()));
            Set<Role> roles = user.getRoles();
            if (roles != null && !roles.isEmpty()) {
                existingUser.getRoles().clear();
                Set<Role> newRoles = getDistinctRoles(roles);
                existingUser.setRoles(newRoles);
            }
            entityManager.merge(existingUser);
        }

    }

    private Set<Role> getDistinctRoles(Set<Role> rolesList) {
        Set<Role> roles = new HashSet<>();
        Set<String> stringRoles = rolesList.stream().map(role -> role.getRole()).collect(Collectors.toSet());
        if (stringRoles.contains("ADMIN")) {
            Role adminRole = entityManager.find(Role.class, 1);
            roles.add(adminRole);
        }
        if (stringRoles.contains("USER")) {
            Role userRole = entityManager.find(Role.class, 2);
            roles.add(userRole);
        }
        return roles;
    }

    private String encodePassword(String rawPassword) {
        PasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String encodedPassword = encoder.encode(rawPassword);
        return encodedPassword;
    }

    @Override
    public void deleteUser(int id) {
        User user = getUserById(id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        List<User> resultList = query.getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

}
