package com.ecommerce.website.controller;

import com.ecommerce.website.configuration.auth.TokenProvider;
import com.ecommerce.website.dto.JwtDto;
import com.ecommerce.website.dto.SignInDto;
import com.ecommerce.website.dto.SignUpDto;
import com.ecommerce.website.enums.UserRole;
import com.ecommerce.website.model.user.User;
import com.ecommerce.website.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserRestControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private UserRestController userRestController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userRestController).build();
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSignUp_Success() throws Exception {
        SignUpDto signUpDto = new SignUpDto("testUser", "password", UserRole.USER);

        User mockUser = new User("testUser", "encryptedPassword", UserRole.USER);
        when(userService.signUp(any(SignUpDto.class))).thenReturn(mockUser);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signUpDto)))
                .andExpect(status().isCreated());
        verify(userService, times(1)).signUp(any(SignUpDto.class));
    }

    @Test
    public void testSignIn_Success() throws Exception {
        SignInDto signInDto = new SignInDto("testUser", "password");

        User mockUser = new User();
        String mockToken = "jdoijdojkkkmzxzajsxzzzzx";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities()));
        when(tokenProvider.generateAccessToken(any(User.class))).thenReturn(mockToken);

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signInDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(new JwtDto(mockToken))));

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, times(1)).generateAccessToken(any(User.class));
    }
}
