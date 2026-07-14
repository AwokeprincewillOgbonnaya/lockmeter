package com.university.passwordchecker.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.university.passwordchecker.persistance.entity.User;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private Long id;
    private String userName;
    private String email;

    public static LoginResponse fromEntity(User user) {
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setUserName(user.getUserName());
        response.setEmail(user.getEmail());

        return response;
    }
}
