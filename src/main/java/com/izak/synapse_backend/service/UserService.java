package com.izak.synapse_backend.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.izak.synapse_backend.DTO.LoginDTO;
import com.izak.synapse_backend.DTO.RegisterDTO;
import com.izak.synapse_backend.entities.Users;
import com.izak.synapse_backend.repositories.UsersRepository;
import com.izak.synapse_backend.security.JWTService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public String registeringUser(RegisterDTO registerDTO) {

        try {
            String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
            Users users = Users
                            .builder()
                            .firstName(registerDTO.getFirstName())
                            .lastName(registerDTO.getLastName())
                            .email(registerDTO.getEmail())
                            .username(registerDTO.getUsername())
                            .password(encodedPassword)
                            .build();
            usersRepository.save(users);
            return jwtService.generateToken(registerDTO.getUsername());

        }  catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        
    }

    public String loginUser(LoginDTO loginDTO) {
        // Implement login logic here
        String token;
        
        try {
            String username = loginDTO.getUsername();
            String password = loginDTO.getPassword();
            
            Optional<Users> user = usersRepository.findByUsername(username);
            boolean isMatch = passwordEncoder.matches(password, user.get().getPassword());
            
            
            if (user.isPresent() && isMatch) {
                // User found and passwords match, proceed with login
                token = jwtService.generateToken(username);
                log.info("User logged in: {}", user.get().getUsername());
            } else {
                // User not found or invalid credentials
                log.warn("Invalid username or password");
                throw new RuntimeException("Invalid username or password");
            }
        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage());
            throw new RuntimeException("Error during login");
        }

        return token;
    }
    
}
