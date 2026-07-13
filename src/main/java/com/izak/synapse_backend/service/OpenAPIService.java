package com.izak.synapse_backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import jakarta.annotation.PostConstruct;

@Service
public class OpenAPIService {
    @Value("${app.openai.api-key}")
    private String apiKey;

    @Value("${app.openai.model:gpt-4o-mini}")
    private String model;

    private RestClient restClient;

    @PostConstruct
    public void init() {
        this.restClient = RestClient
                .builder()
                .baseUrl("https://api.openai.com/v1/chat")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public String sendMessage(String message) {
        // Implement the logic to send a message to the OpenAI API using the restClient
        // You can use the model and apiKey for any necessary API calls

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", new Object[] { new HashMap<String, String>() {{
            put("role", "user");
            put("content", message);
        }} });

        System.out.println("Request Body: " + apiKey + " "+ model + " " + requestBody);

        Map<String, Object> response = restClient.post()
                .uri("/completions")
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        List<Map<String,Object>> choices =
                (List<Map<String,Object>>)
                        response.get("choices");

        Map<String,Object> choice =
                choices.get(0);

        Map<String,Object> responseMessage =
                (Map<String,Object>)
                        choice.get("message");

        return responseMessage.get("content").toString();
    }
}
