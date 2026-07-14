package com.university.passwordchecker.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record CreateUserRequest(
        @NotBlank(message = "User name is required") String userName,
        @NotBlank(message = "Email is required ") @Email(message = "Enter valid Email") String email,
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters") String password,
        @NotBlank(message = "Please confirm your password") String confirmPassword
) {
}
