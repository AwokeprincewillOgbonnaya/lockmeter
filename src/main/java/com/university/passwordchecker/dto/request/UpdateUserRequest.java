package com.university.passwordchecker.dto.request;


import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank(message = "User name is required") String userName
) {
}
