package com.example.project_two.dto;

import java.util.List;

import com.example.project_two.entity.Post;

import lombok.Data;

@Data
public class UserResponse {
    private Long userId;
    private String userEmail;
    private String bio;
    private String profileImageUrl;
    private String displayName;
    private int followers;
    private int following;
    private boolean followedByCurrentUser;
    private List<Post> posts;
}
