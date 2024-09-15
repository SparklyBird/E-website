package com.ecommerce.website.service;

import com.ecommerce.website.dao.user.UserRepository;
import com.ecommerce.website.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void registerUser(String email, String password) {

        if (userRepository.findByLogin(email).isPresent()) {
            throw new RuntimeException("User already exists");
        }


        User newUser = new User();
        newUser.setLogin(email);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);
    }
}