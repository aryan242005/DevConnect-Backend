package com.example.project_two.exception;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String message){
        super(message);
    }
}
