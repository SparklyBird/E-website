package com.ecommerce.website.controller;

import com.ecommerce.website.configuration.auth.TokenProvider;
import com.ecommerce.website.dto.JwtDto;
import com.ecommerce.website.dto.SignInDto;
import com.ecommerce.website.dto.SignUpDto;
import com.ecommerce.website.exception.InvalidJwtException;
import com.ecommerce.website.model.user.User;
import com.ecommerce.website.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
public class UserRestController {
    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);
    private final AuthenticationManager authenticationManager;
    private final UserService service;
    private final TokenProvider tokenService;

    @Autowired
    public UserRestController(AuthenticationManager authenticationManager, UserService service, TokenProvider tokenService) {
        this.authenticationManager = authenticationManager;
        this.service = service;
        this.tokenService = tokenService;
    }

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody @Valid SignUpDto data) throws InvalidJwtException {
        service.signUp(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtDto> signIn(@RequestBody @Valid SignInDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var authUser = authenticationManager.authenticate(usernamePassword);
        var accessToken = tokenService.generateAccessToken((User) authUser.getPrincipal());
        return ResponseEntity.ok(new JwtDto(accessToken));
    }
}
