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
public class ConVoModels {
    private String id;   
    private String title; 
    
    @Builder.Default
    private String timeStamp = LocalDateTime.now().toString();
}
