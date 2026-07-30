package com.izak.synapse_backend.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.izak.synapse_backend.DTO.ChatDTO;
import com.izak.synapse_backend.constants.AppConstants;
import com.izak.synapse_backend.entities.Users;
import com.izak.synapse_backend.repositories.UsersRepository;
import com.izak.synapse_backend.security.JWTService;
import com.izak.synapse_backend.service.OllamaService;
import com.izak.synapse_backend.service.OpenAPIService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final OpenAPIService openAPIService;
    private final OllamaService ollamaService;
    private final JWTService jwtService;
    private final UsersRepository usersRepository;

    @Value("${app.llm}")
    private String llm;
    

    @GetMapping("/chat")
    public String getChat() {
        return llm;
    }

    @GetMapping("/userDetails")
    public ResponseEntity<Object> getMethodName(HttpServletRequest request) {

        Map<String, String> messageResponse = new HashMap<>();
        int statusCode = 400;
        String resp = "";

        String token = request
            .getHeader(AppConstants.AUTHORIZATION)
            .substring(AppConstants.SUBSTRING);

        String username = jwtService.extractUsername(token);
        Optional<Users> user = usersRepository.findByUsername(username);
        
        
        if (user.isPresent()) {
            resp = user.get().getUsername();
            statusCode = 200;
        }

        messageResponse.put("username", resp);
        

        return ResponseEntity.status(statusCode).body(messageResponse);
    }

    @PostMapping("/chat")
    public ResponseEntity<Object> postChat(@Valid @RequestBody ChatDTO chatDTO) {
        
        Map<String, String> messageResponse = new HashMap<>();
        int statusCode = 200;
        try {
            System.out.println("Received message: " + chatDTO.getMessage());
            messageResponse.put("message", "Chat endpoint");
            messageResponse.put("response", llm.equals("ollama") ? ollamaService.sendMessage(chatDTO.getMessage()) : openAPIService.sendMessage(chatDTO.getMessage()));
            
        } catch (Exception e) {
            log.error("Error during chat processing: {}", e.getMessage());
            statusCode = 500;
            messageResponse.put("status", "failure");
        }


        return ResponseEntity.status(statusCode).body(messageResponse);
    }
}
