package com.example.project_two.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_two.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserUserIdInOrderByCreatedAtDesc(List<Long> userIds);
    List<Post> findByUserUserId(Long userId);
    Page<Post> findByUserUserIdIn(List<Long> followingIds, Pageable pageable);
}
