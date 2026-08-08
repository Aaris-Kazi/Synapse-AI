package com.izak.synapse_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.izak.synapse_backend.entities.Conversations;

public interface ConversationRepository extends JpaRepository<Conversations, Long> {
    // Add custom query methods if needed
    Optional<Conversations> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
