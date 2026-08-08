package com.izak.synapse_backend.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConVoModels {
    private String id;   
    private String title; 
    private LocalDateTime timeStamp = LocalDateTime.now();
}
