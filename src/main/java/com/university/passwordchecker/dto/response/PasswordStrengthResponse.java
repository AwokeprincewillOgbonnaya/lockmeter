package com.university.passwordchecker.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordStrengthResponse {
    private String strength;
    private int score;
    private int length;
    private boolean upperCase;
    private  boolean lowerCase;
    private boolean hasNumber;
    private boolean hasSpecialCharacter;
    private String feedback;
}
