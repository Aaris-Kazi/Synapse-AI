package com.izak.synapse_backend.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.izak.synapse_backend.DTO.ChatDTO;
import com.izak.synapse_backend.DTO.MessageModel;
import com.izak.synapse_backend.constants.AppConstants;
import com.izak.synapse_backend.entities.ChatModel;
import com.izak.synapse_backend.entities.Users;
import com.izak.synapse_backend.repositories.ChatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

    @Value("${app.llm}")
    private String llm;

    private final OpenAPIService openAPIService;
    private final OllamaService ollamaService;
    private final ChatRepository chatRepository;

    public MessageModel makeMessage(String role, String message) {
        return MessageModel
                .builder()
                .role(role)
                .content(message)
                .build();
    }

    public String chat(ChatDTO chatDTO, Users user) {

        String response = llm.equals("ollama") ? ollamaService.sendMessage(chatDTO.getMessage())
                : openAPIService.sendMessage(chatDTO.getMessage());

        

        ChatModel chatModel = chatRepository
                .findByConversationID(chatDTO.getMessageID())
                .orElseGet(() -> {
                    String responseTitle = llm.equals("ollama") ? ollamaService.sendMessage("Please make 3 words title for this conversation " + response)
                            : openAPIService.sendMessage("Please make 3 words title for this conversation " + response);
                    ChatModel newChatModel = ChatModel
                            .builder()
                            .conversationID(chatDTO.getMessageID())
                            .userId(user.getId())
                            .title(responseTitle)
                            .createdAt(LocalDateTime.now())
                            .build();
                    return chatRepository.save(newChatModel);
                });

        chatModel
                .getMessages()
                .add(makeMessage(AppConstants.USER, chatDTO.getMessage()));

        chatModel
                .getMessages()
                .add(makeMessage(AppConstants.AGENT, response));

        chatModel.setUpdatedAt(LocalDateTime.now());

        chatRepository.save(chatModel);

        return response;
    }

}
