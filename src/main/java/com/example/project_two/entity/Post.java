package com.example.project_two.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="posts")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @NotBlank(message = "Title cannot be null")
    private String postTitle;

    @NotBlank(message = "Content cannot be empty")
    @Size(min = 50, max = 5000, message="Post content must be between 50 to 5000 characters")
    @Column(columnDefinition = "TEXT")
    private String postContent;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy="post", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy="post", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<Like> likes;

    @Column(nullable=true)
    private String postImageUrl;
}
