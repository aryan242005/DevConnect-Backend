package com.example.project_two.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="comments")
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    Long commentId;

    @NotBlank(message="Comment cannot be blank")
    String commentContent;

    LocalDateTime commentCreatedAt;

    String userName;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id")
    @JsonIgnore
    @ToString.Exclude
    Post post;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    @ToString.Exclude
    User user;
}
