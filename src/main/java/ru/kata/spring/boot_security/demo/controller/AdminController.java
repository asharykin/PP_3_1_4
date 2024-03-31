package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;


@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String getAllUsers(ModelMap model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @PostMapping("/admin/add")
    public String addUser(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                          @RequestParam("age") int age, @RequestParam("email") String email,
                          @RequestParam("password") String password, @RequestParam("roles") List<String> roles) {
        if (userService.getUserByEmail(email) == null) {
            User user = new User(firstName, lastName, age, email, password);
            userService.addUser(user, roles);
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/update")
    public String updateUser(@RequestParam("id") int id, @RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName, @RequestParam("age") String age,
                             @RequestParam("email") String email, @RequestParam("password") String password,
                             @RequestParam(name = "roles", required = false) List<String> roles) {
        User user = userService.getUserById(id);
        if (user != null) {
            user.setFirstName(firstName.isEmpty() ? user.getFirstName() : firstName);
            user.setLastName(lastName.isEmpty() ? user.getLastName() : lastName);
            user.setAge(age.isEmpty() ? user.getAge() : Integer.parseInt(age));
            user.setEmail(email.isEmpty() || userService.getUserByEmail(email) != null ? user.getEmail() : email);
            user.setPassword(password.isEmpty() ? user.getPassword() : password);
            userService.updateUser(user, roles);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
