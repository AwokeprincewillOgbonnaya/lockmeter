package com.university.passwordchecker.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PasswordCheckRequest(
@NotBlank(message ="password is required") String password
) {
}
