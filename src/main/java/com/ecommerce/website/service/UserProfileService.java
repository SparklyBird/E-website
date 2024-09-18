package com.ecommerce.website.service;

import com.ecommerce.website.dao.user.UserProfileRepository;
import com.ecommerce.website.dao.user.UserRepository;
import com.ecommerce.website.model.user.User;
import com.ecommerce.website.model.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    private final UserRepository userRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }


    public Optional<UserProfile> getProfileByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }


    public UserProfile saveUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }


    public Optional<UserProfile> findById(Long profileId) {
        return userProfileRepository.findById(profileId);
    }


    public UserProfile createUserProfile(Long userId, UserProfile newProfile) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        newProfile.setUser(user.get());
        return userProfileRepository.save(newProfile);
    }


    public UserProfile updateUserProfile(UserProfile updatedProfile) {
        return userProfileRepository.save(updatedProfile);
    }
}
