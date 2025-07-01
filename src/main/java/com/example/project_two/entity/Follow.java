package com.example.project_two.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="follow", indexes={
    @Index(name="idx_follower_id", columnList = "follower_id"),
    @Index(name="idx_following_id", columnList = "following_id")
})
public class Follow {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long followId;

    @ManyToOne
    @JoinColumn(name="follower_id")
    private User follower;

    @ManyToOne
    @JoinColumn(name="following_id")
    private User following;
}
