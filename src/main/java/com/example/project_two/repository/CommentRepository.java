package com.example.project_two.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.project_two.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    @Query("SELECT c FROM Comment c WHERE c.post.postId = :postId")
    List<Comment> getCommentsByPostId(Long postId);
}
