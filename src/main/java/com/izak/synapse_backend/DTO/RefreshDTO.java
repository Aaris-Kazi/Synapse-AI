package com.izak.synapse_backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class RefreshDTO {
    @NotNull (message = "Refresh token cannot be null")
    @NotBlank (message = "Refresh token is required")
    private String refreshToken;
    
}
