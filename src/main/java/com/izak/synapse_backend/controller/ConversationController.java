package com.izak.synapse_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.izak.synapse_backend.DTO.ConVoModels;
import com.izak.synapse_backend.constants.AppConstants;
import com.izak.synapse_backend.entities.Users;
import com.izak.synapse_backend.repositories.UsersRepository;
import com.izak.synapse_backend.security.JWTService;
import com.izak.synapse_backend.service.ChatService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class ConversationController {

    
    private final JWTService jwtService;
    private final UsersRepository usersRepository;
    private final ChatService chatService;
    
    @GetMapping("/conversationList")
    public ResponseEntity<Map<String, Object>> getMethodName(HttpServletRequest request) {
        Map<String, Object> messageResponse = new HashMap<>();
        int statusCode = 200;

        String token = request
            .getHeader(AppConstants.AUTHORIZATION)
            .substring(AppConstants.SUBSTRING);

        String username = jwtService.extractUsername(token);
        Optional<Users> user = usersRepository.findByUsername(username);

        if (user.isPresent()) {
            List<ConVoModels> conversations = chatService.getConversationsList(user.get());
            messageResponse.put("message", "Conversation list fetched successfully");
            messageResponse.put("conversations", conversations);
        } else {
            messageResponse.put("message", "User not found");
            statusCode = 404;
        }

        return ResponseEntity.status(statusCode).body(messageResponse);
    }
    
}
