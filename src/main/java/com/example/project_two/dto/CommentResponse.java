package com.example.project_two.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentResponse {
    private Long commentId;
    private String commentContent;
    private LocalDateTime commentCreatedAt;
    private UserResponse userResponse;
}
