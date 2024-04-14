package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/admin")
    public String getAdminPage() {
        return "admin_2";
    }

    @GetMapping("/user")
    public String getUserPage() {
        return "user_2";
    }
}
