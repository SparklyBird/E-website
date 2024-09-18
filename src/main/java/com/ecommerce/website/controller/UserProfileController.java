package com.ecommerce.website.controller;

import com.ecommerce.website.dto.UserProfileDTO;
import com.ecommerce.website.model.user.User;
import com.ecommerce.website.model.user.UserProfile;
import com.ecommerce.website.service.UserProfileService;
import com.ecommerce.website.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private final UserService userService;
    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService, UserService userService) {
        this.userProfileService = userProfileService;
        this.userService = userService;
    }

    @PostMapping("/update")
    public UserProfileDTO updateUserProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserProfileDTO updatedProfile) {
        User user = userService.findByUsername(ofNullable(userDetails.getUsername()).orElseThrow(()
                -> new RuntimeException("User not found")));
        UserProfile profile = ofNullable(user.getProfile()).orElse(new UserProfile());

        profile.setFirstName(updatedProfile.getFirstName());
        profile.setLastName(updatedProfile.getLastName());
        profile.setEmail(updatedProfile.getEmail());
        profile.setPhoneNumber(updatedProfile.getPhoneNumber());
        profile.setUser(user);

        UserProfile profileAfterUpdate = userProfileService.updateUserProfile(profile);
        return new UserProfileDTO(profileAfterUpdate);
    }


    @PostMapping("/create")
    public UserProfileDTO createUserProfile(@RequestBody UserProfile profile, @RequestParam Long userId) {
        User user = userService.findById(userId);
        profile.setUser(user);

        UserProfile profileAfterUpdate = userProfileService.updateUserProfile(profile);
        return new UserProfileDTO(profileAfterUpdate);
    }
}
