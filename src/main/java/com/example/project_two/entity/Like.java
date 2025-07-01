package com.example.project_two.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="likes")
@Data
public class Like {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long likeId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id")
    @JsonIgnore
    @ToString.Exclude
    private Post post;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;
}
