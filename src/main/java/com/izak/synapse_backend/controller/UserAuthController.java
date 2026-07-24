package com.izak.synapse_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.izak.synapse_backend.DTO.GoogleAuthRequest;
import com.izak.synapse_backend.DTO.LoginDTO;
import com.izak.synapse_backend.DTO.RegisterDTO;
import com.izak.synapse_backend.service.GoogleAuthService;
import com.izak.synapse_backend.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth/v1")
@Slf4j
@AllArgsConstructor
public class UserAuthController {

    private final UserService userService;
    private final GoogleAuthService googleAuthService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDTO) {

        Map<String, String> messageResponse = new HashMap<>();
        int statusCode = 200;
        try {
            String token = userService.loginUser(loginDTO);
            messageResponse.put("message", "User logged in successfully");
            messageResponse.put("access-token", token);

        } catch (Exception e) {
            statusCode = 401; // Unauthorized
            messageResponse.put("status", "failure");
            messageResponse.put("message", "Invalid username or password");
        }
        return ResponseEntity.status(statusCode).body(messageResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterDTO requestDTO) {

        // Encode the password before saving
        Map<String, String> messageResponse = new HashMap<>();
        int statusCode = 200;

        try {
            String token = userService.registeringUser(requestDTO);

            messageResponse.put("status", "pass");
            messageResponse.put("message", "User registered successfully");
            messageResponse.put("access-token", token);
        } catch (Exception e) {
            messageResponse.put("status", "failure");
            messageResponse.put("message", "Internal server error");
            statusCode = 500;
        }
        return ResponseEntity.status(statusCode).body(messageResponse);
    }

    @PostMapping("/googleLogin")
    public ResponseEntity<Object> googleLogin(@Valid @RequestBody GoogleAuthRequest requestDTO) {
        //TODO: process POST request
        
        Map<String, String> messageResponse = new HashMap<>();
        int statusCode = 200;

        try {
            messageResponse.put("status", "pass");
            String token = googleAuthService.authenticateWithGoogle(requestDTO);
            messageResponse.put("access-token", token);
        } catch (Exception e) {
            log.error("Error during Google login: {}", e.getMessage());
            messageResponse.put("status", "failure");
            messageResponse.put("message", "Internal server error");
            statusCode = 500;
        }

        return ResponseEntity.status(statusCode).body(messageResponse);
    }
    

}
