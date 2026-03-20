package com.example.myapp.controller;

import com.example.myapp.model.User;
import com.example.myapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Show Register Page
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Handle Register Form
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user,
                               BindingResult result,
                               Model model) {
        // Show validation errors
        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(user);
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // Show Login Page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Show Profile Page
    @GetMapping("/profile/{id}")
    public String showProfile(@PathVariable Long id, Model model) {
        userService.getUserById(id).ifPresent(user -> model.addAttribute("user", user));
        return "profile";
    }

    // Update Profile
    @PostMapping("/profile/{id}")
    public String updateProfile(@PathVariable Long id,
                                @Valid @ModelAttribute User updatedUser,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "profile";
        }
        userService.getUserById(id).ifPresent(user -> {
            user.setName(updatedUser.getName());
            user.setPhone(updatedUser.getPhone());
            userService.updateUser(user);
        });
        return "redirect:/profile/" + id + "?updated";
    }
    // Home Page - Redirect to items
@GetMapping("/")
public String home() {
    return "redirect:/items";
}
}