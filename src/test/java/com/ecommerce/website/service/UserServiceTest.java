package com.ecommerce.website.service;

import com.ecommerce.website.dao.user.UserRepository;
import com.ecommerce.website.dto.SignUpDto;
import com.ecommerce.website.enums.UserRole;
import com.ecommerce.website.exception.InvalidJwtException;
import com.ecommerce.website.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_Success() {
        String username = "testUser";
        User mockUser = new User(username, "encryptedPassword", UserRole.USER);
        when(userRepository.findByLogin(username)).thenReturn(Optional.of(mockUser));
        UserDetails userDetails = userService.loadUserByUsername(username);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String username = "unknownUser";
        when(userRepository.findByLogin(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    public void testSignUp_Success() throws InvalidJwtException {
        SignUpDto signUpDto = new SignUpDto("testUser", "password", UserRole.ADMIN);
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());
        User savedUser = new User("testUser", "encryptedPassword", UserRole.ADMIN);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        UserDetails userDetails = userService.signUp(signUpDto);
        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    public void testSignUp_UserAlreadyExists() {
        SignUpDto signUpDto = new SignUpDto("existingUser", "password", UserRole.USER);
        when(userRepository.findByLogin(signUpDto.login())).thenReturn(Optional.of(new User()));
        assertThrows(InvalidJwtException.class, () -> userService.signUp(signUpDto));
    }

    @Test
    public void testFindById_Success() {
        Long userId = 1L;
        User mockUser = new User("testUser", "encryptedPassword", UserRole.USER);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        User user = userService.findById(userId);
        assertEquals("testUser",
                user.getUsername());
    }

    @Test
    public void testFindById_UserNotFound() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.findById(userId));
    }
}
