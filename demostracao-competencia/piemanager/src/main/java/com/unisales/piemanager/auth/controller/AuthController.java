package com.unisales.piemanager.auth.controller;

import com.unisales.piemanager.auth.dto.LoginRequest;
import com.unisales.piemanager.auth.dto.LoginResponse;
import com.unisales.piemanager.common.api.ApiResponse;
import com.unisales.piemanager.user.UserService;
import com.unisales.piemanager.user.model.User;
import com.unisales.piemanager.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().trim().toLowerCase(), request.getPassword()));

        User user = userService.findEntityByEmail(request.getEmail());
        String token = jwtService.generateToken(user.getEmail());

        LoginResponse response = new LoginResponse();
        response.setTokenType("Bearer");
        response.setAccessToken(token);
        response.setExpiresIn(jwtService.getExpirationSeconds());
        response.setUser(userService.toResponse(user));

        return ApiResponse.success("Login successful", response);
    }
}
