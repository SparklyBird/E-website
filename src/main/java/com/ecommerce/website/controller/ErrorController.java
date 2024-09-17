package com.ecommerce.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    // Mapping for access-denied page
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // Return the Thymeleaf template for access denied
    }
}
