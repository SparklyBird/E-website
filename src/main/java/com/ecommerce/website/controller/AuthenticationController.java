package com.ecommerce.website.controller;

import com.ecommerce.website.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "user/login"; // Points to login.html in templates
    }

    @GetMapping("/register")
    public String registerPage() {
        return "user/register"; // Points to register.html in templates
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password, RedirectAttributes redirectAttributes) {
        try {
            authenticationService.registerUser(email, password);
            return "redirect:/login"; // Redirect to login page on successful registration
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed.");
            return "redirect:/register?error"; // Redirect back to register page with error message
        }
    }
}
