package com.university.passwordchecker.exception;

public class PasswordCheckException extends RuntimeException{
    private final int statusCode;

    public PasswordCheckException( String message,int statusCode){
        super(message);
        this.statusCode=statusCode;
    }
    public int getStatusCode(){
        return statusCode;
    }
}
