package com.izak.synapse_backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.izak.synapse_backend.DTO.ChatDTO;
import com.izak.synapse_backend.DTO.ConVoModels;
import com.izak.synapse_backend.DTO.MessageModel;
import com.izak.synapse_backend.constants.AppConstants;
import com.izak.synapse_backend.entities.ChatModel;
import com.izak.synapse_backend.entities.Conversations;
import com.izak.synapse_backend.entities.Users;
import com.izak.synapse_backend.repositories.ChatRepository;
import com.izak.synapse_backend.repositories.ConversationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

        @Value("${app.llm}")
        private String llm;

        private final OpenAPIService openAPIService;
        private final OllamaService ollamaService;
        private final ChatRepository chatRepository;
        private final ConversationRepository conversationRepository;

        public MessageModel makeMessage(String role, String message) {
                return MessageModel
                                .builder()
                                .role(role)
                                .content(message)
                                .build();
        }

        @Transactional
        public Map<String, String> chat(ChatDTO chatDTO, Users user) {

                String response = llm.equals("ollama") ? ollamaService.sendMessage(chatDTO.getMessage())
                                : openAPIService.sendMessage(chatDTO.getMessage());

                ChatModel chatModel = chatRepository
                                .findByConversationID(chatDTO.getMessageID())
                                .orElseGet(() -> {
                                        String responseTitle = llm.equals("ollama") ? ollamaService
                                                        .sendMessage("Please make 3 words title for this conversation "
                                                                        + chatDTO.getMessage())
                                                        : openAPIService.sendMessage(
                                                                        "Please make 3 words title for this conversation "
                                                                                        + chatDTO.getMessage());
                                        Conversations conversations = conversationRepository
                                                        .findByUserId(user.getId())
                                                        .orElseGet(() -> {
                                                                Conversations newConversations = Conversations
                                                                                .builder()
                                                                                .user(user)
                                                                                .build();
                                                                return conversationRepository.save(newConversations);
                                                        });

                                        List<ConVoModels> existingConversations = conversations.getConversations();
                                        boolean conversationExists = existingConversations.stream()
                                                        .anyMatch(convo -> convo.getId().equals(chatDTO.getMessageID()));
                                        if (!conversationExists) {
                                                conversations.getConversations().add(
                                                                ConVoModels
                                                                                .builder()
                                                                                .id(chatDTO.getMessageID())
                                                                                .title(responseTitle)
                                                                        .build());
                                        }
                                        conversationRepository.save(conversations);
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

                return new HashMap<String, String>() {
                        {
                                put("response", response);
                                put("conversationID", chatModel.getConversationID());
                                put("title", chatModel.getTitle());
                        }
                };
        }

        public Map<String, Object> getChat(String messageID, Users user) {
                // Implementation for retrieving chat

                ChatModel chatModel = chatRepository
                                .findByUserIdAndConversationID(user.getId(), messageID)
                                .orElseThrow(() -> {
                                        throw new RuntimeException("Chat not found for messageID: " + messageID);
                                });

                return new HashMap<String, Object>() {
                        {
                                put("conversationID", chatModel.getConversationID());
                                put("title", chatModel.getTitle());
                                put("messages", chatModel.getMessages());
                        }
                };
        }

        public List<ConVoModels> getConversationsList(Users user) {
                Optional<Conversations> conversations = conversationRepository.findByUserId(user.getId());

                if (conversations.isPresent()) {
                        Conversations userConversations = conversations.get();
                        return userConversations.getConversations();
                } else {
                        return new ArrayList<>();
                }
        }
}
