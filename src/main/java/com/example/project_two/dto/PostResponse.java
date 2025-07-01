package com.example.project_two.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class PostResponse {
    
    private Long postId;
    private String postTitle;
    private String postContent;
    private LocalDateTime createdAt;
    private String postImageUrl;
    private String userName;
    private List<CommentResponse> comments;
    private boolean likedByCurrentUser;
    private int likeCount;
    private Long userId;
    private UserResponse user;
}
