package com.example.project_two.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message="Email is required")
    @Email(message="Email should be valid")
    String userEmail;

    String userPassword;
}
