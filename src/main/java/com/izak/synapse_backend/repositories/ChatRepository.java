package com.izak.synapse_backend.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.izak.synapse_backend.entities.ChatModel;
import java.util.List;


public interface ChatRepository extends MongoRepository<ChatModel, String> {

    Optional<ChatModel>  findByConversationID(String conversationID);
    Optional<ChatModel>  findByUserIdAndConversationID(Long userId, String conversationID);
    List<ChatModel> findByUserIdOrderByUpdatedAtDesc(Long userId);
}
