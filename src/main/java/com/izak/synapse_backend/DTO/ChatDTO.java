package com.izak.synapse_backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatDTO {
    @NotNull(message = "Message cannot be null")
    @NotBlank(message = "Message is required")
    String message;    
}
