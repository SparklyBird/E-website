package com.ecommerce.website.service;

import com.ecommerce.website.dao.user.UserRepository;
import com.ecommerce.website.dto.SignUpDto;
import com.ecommerce.website.exception.InvalidJwtException;
import com.ecommerce.website.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
    {
        return repository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    public User findByUsername(String username)
    {
        return repository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    public UserDetails signUp(SignUpDto data) throws InvalidJwtException {
        if (repository.findByLogin(data.login()).isPresent()) {
            logger.error("Username already exists");
            throw new InvalidJwtException("Username already exists");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, data.role());
        return repository.save(newUser);
    }

    public User findById(Long userId) {
        return repository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
}
