package com.izak.synapse_backend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDTO {

    String firstName;
    String lastName;
    @NotBlank(message = "Username is required")
    String username;
    @NotBlank(message = "Email is required")
    String email;
    @NotBlank(message = "Password is required")
    String password;
}
