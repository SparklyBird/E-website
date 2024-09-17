package com.ecommerce.website.controller;

import com.ecommerce.website.dto.UserProfileDTO;
import com.ecommerce.website.model.user.User;
import com.ecommerce.website.model.user.UserProfile;
import com.ecommerce.website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static java.util.Optional.ofNullable;

@Controller
@RequestMapping("/profile")
public class UserProfilePageController {

    private final UserService userService;

    @Autowired
    public UserProfilePageController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/view")
    public String viewProfilePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        User user = userService.findByUsername(
                ofNullable(userDetails.getUsername())
                        .orElseThrow(() -> new RuntimeException("User not found"))
        );


        UserProfile profile = user.getProfile();
        UserProfileDTO profileDTO;

        if (profile != null) {

            profileDTO = new UserProfileDTO(profile);
        } else {

            profileDTO = new UserProfileDTO();
        }

        model.addAttribute("profile", profileDTO);


        return "user/userprofile";
    }
}
