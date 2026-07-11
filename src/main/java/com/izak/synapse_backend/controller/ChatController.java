package com.izak.synapse_backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.izak.synapse_backend.DTO.ChatDTO;
import com.izak.synapse_backend.service.OpenAPIService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final OpenAPIService openAPIService;
    

    @GetMapping("/chat")
    public String getChat() {
        return "Chat endpoint";
    }

    @PostMapping("/chat")
    public ResponseEntity<Object> postChat(@Valid @RequestBody ChatDTO chatDTO) {
        
        Map<String, String> messageResponse = new HashMap<>();
        int statusCode = 200;
        try {
            System.out.println("Received message: " + chatDTO.getMessage());
            messageResponse.put("message", "Chat endpoint");
            messageResponse.put("response", openAPIService.sendMessage(chatDTO.getMessage()));
            
        } catch (Exception e) {
            log.error("Error during chat processing: {}", e.getMessage());
            statusCode = 500;
            messageResponse.put("status", "failure");
        }


        return ResponseEntity.status(statusCode).body(messageResponse);
    }
}
