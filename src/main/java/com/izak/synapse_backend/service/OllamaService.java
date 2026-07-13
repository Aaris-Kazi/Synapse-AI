package com.izak.synapse_backend.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import jakarta.annotation.PostConstruct;

@Service
public class OllamaService {
    @Value("${app.ollama.api-key}")
    private String apiKey;

    @Value("${app.ollama.model:gpt-oss:120b}")
    private String model;

    @Value("${app.ollama.base-url}")
    private String baseUrl;

    private RestClient restClient;

    @PostConstruct
    public void init() {
        this.restClient = RestClient
                .builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public String sendMessage(String message) {
        // Implement the logic to send a message to the Ollama API using the restClient
        // You can use the model and apiKey for any necessary API calls

        String requestBody = String.format(
                "{\"model\": \"%s\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}],\"stream\": false}", model, message);

        System.out.println("Request Body: " + requestBody);

        Map<String, Object> response =  restClient.post()
                .uri("/api/chat")
                .body(requestBody)
                .retrieve()
                .body(Map.class);
        Map<String, Object> responseMessage = (Map<String, Object>) response.get("message");
        return responseMessage.get("content").toString();
    }
}
