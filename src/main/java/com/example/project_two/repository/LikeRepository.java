package com.example.project_two.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.project_two.entity.Like;
import com.example.project_two.entity.Post;
import com.example.project_two.entity.User;

public interface LikeRepository extends JpaRepository<Like, Long>{
    Optional<Like> findByUserAndPost(User user, Post post);
    @Query("SELECT l.post.postId FROM Like l WHERE l.user.userId = :userId")
    Set<Long> findLikedPostByUserId(Long userId);
    @Query("SELECT COUNT(l) FROM Like l WHERE l.post.postId = :postId")
    int countByPostId(Long postId);
}
