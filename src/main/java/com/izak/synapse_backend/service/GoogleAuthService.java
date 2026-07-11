package com.izak.synapse_backend.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.webtoken.JsonWebToken.Payload;
import com.izak.synapse_backend.DTO.GoogleAuthRequest;
import com.izak.synapse_backend.entities.Users;
import com.izak.synapse_backend.repositories.UsersRepository;
import com.izak.synapse_backend.security.JWTService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {
    
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    // @Value("${oauth2.client.registration.google.client-secret}")
    // private String clientSecret;

    private final UsersRepository repository;
    private final JWTService jwtService;

    public String authenticateWithGoogle(GoogleAuthRequest googleAuthRequest) throws GeneralSecurityException, IOException {
        // Implement the logic to authenticate with Google using the provided token
        // You can use the clientId and clientSecret for any necessary API calls
        String token = googleAuthRequest.getToken();

        
        GoogleIdTokenVerifier tokenVerifier = new GoogleIdTokenVerifier
            .Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();


        GoogleIdToken payload = tokenVerifier.verify(token);

        if (payload == null) {
            throw new GeneralSecurityException("Invalid Google ID token");
        }

        Payload tokenPayload = payload.getPayload();

        String googleId = tokenPayload.getSubject();
        String email = tokenPayload.get("email").toString();

        String firstName = tokenPayload.get("given_name").toString();
        String picture = tokenPayload.get("picture").toString();

        Users user = repository.findByEmail(email)
                .orElseGet(() -> {
                    Users newUser = new Users();
                    newUser.setGoogleId(googleId);
                    newUser.setEmail(email);
                    newUser.setFirstName(firstName);
                    newUser.setUsername(email);
                    newUser.setPicture(picture);
                    return repository.save(newUser);
                });

        return jwtService.generateToken(user.getUsername());
    }
}
