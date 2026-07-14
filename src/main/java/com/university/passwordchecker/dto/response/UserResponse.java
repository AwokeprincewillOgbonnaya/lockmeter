package com.university.passwordchecker.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.university.passwordchecker.persistance.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private Long id;
    private String userName;
    private String email;
    private LocalDateTime createdAt;


    public static UserResponse fromEntity(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUserName(user.getUserName());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
