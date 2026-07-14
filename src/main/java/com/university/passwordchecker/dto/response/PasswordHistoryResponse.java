package com.university.passwordchecker.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.university.passwordchecker.persistance.entity.PasswordHistory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PasswordHistoryResponse {
    private Long id;
    private String userName;
    private String generatedPassword;
    private LocalDateTime createdAt;

    public static PasswordHistoryResponse fromEntity (PasswordHistory passwordHistory){
        PasswordHistoryResponse response = new PasswordHistoryResponse();
        response.setId(passwordHistory.getId());
        response.setUserName(passwordHistory.getUser().getUserName());
        response.setGeneratedPassword(passwordHistory.getGeneratedPassword());
        response.setCreatedAt(passwordHistory.getCreatedAt());
        return response;
    }
}
