package com.ecommerce.website.controller;

import com.ecommerce.website.dto.SignInDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/login")
    public String loginPage(Model theModel) {
        SignInDto signInDto = new SignInDto("","");
        theModel.addAttribute("signInDto", signInDto);
        return "login";
    }
}