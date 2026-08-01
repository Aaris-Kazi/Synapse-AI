package com.izak.synapse_backend.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageModel {
    String role;
    String content;
    
    @Builder.Default
    LocalDateTime timeStamp = LocalDateTime.now();

}
