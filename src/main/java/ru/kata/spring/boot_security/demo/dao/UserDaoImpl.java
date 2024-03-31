package ru.kata.spring.boot_security.demo.dao;


import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public void addUser(User user, List<String> rolesList) {
        if (rolesList != null) {
            Set<Role> roles = new HashSet<>();
            addRolesToSet(rolesList, roles);
            user.setRoles(roles);
        }
        entityManager.persist(user);
    }

    @Override
    public void updateUser(User user, List<String> rolesList) {
        if (rolesList != null) {
            Set<Role> roles = user.getRoles();
            roles.clear();
            addRolesToSet(rolesList, roles);
        }
        entityManager.merge(user);
    }

    private void addRolesToSet(List<String> rolesList, Set<Role> roles) {
        if (rolesList.contains("admin")) {
            Role adminRole = entityManager.find(Role.class, 1);
            roles.add(adminRole);
        }
        if (rolesList.contains("user")) {
            Role userRole = entityManager.find(Role.class, 2);
            roles.add(userRole);
        }
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
