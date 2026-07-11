package com.izak.synapse_backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleAuthRequest {

    @NotNull(message = "Token cannot be null")
    @NotBlank(message = "Token is required")
    private String token;    
}
