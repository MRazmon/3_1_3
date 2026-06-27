package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.service.UserService;


@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String homePage() {
        return "home";
    }


    @GetMapping("/user")
    public String userPage(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            var currentUser = userService.findByEmail(auth.getName());
            model.addAttribute("user", currentUser);
            model.addAttribute("currentUser", currentUser);
        }
        return "user";
    }
}

