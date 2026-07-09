package com.izak.synapse_backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDTO {

    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username is required")
    String username;
    @NotBlank(message = "Password is required")
    String password;
}
