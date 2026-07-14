package com.university.passwordchecker.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse<T> {
    private String message;
    private T data;
    private int statusCode;

    public GenericResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
