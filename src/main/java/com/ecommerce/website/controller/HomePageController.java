package com.ecommerce.website.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {
    private static Logger logger = LogManager.getLogger(HomePageController.class);

    @GetMapping("/")
    public String homePage(Model theModel) {
        return "homePage";
    }
}
