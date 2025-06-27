package com.example.project_two.service;

import org.springframework.stereotype.Service;

import com.example.project_two.entity.Like;
import com.example.project_two.entity.Post;
import com.example.project_two.entity.User;
import com.example.project_two.exception.PostNotFoundException;
import com.example.project_two.exception.UserNotFoundException;
import com.example.project_two.repository.LikeRepository;
import com.example.project_two.repository.PostRepository;
import com.example.project_two.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public boolean toggleLike(Long postId, String userEmail) {
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User with email : " + userEmail + " not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post with id: " + postId + " not found"));

        return likeRepository.findByUserAndPost(user, post).map(existingLike -> {
            likeRepository.delete(existingLike);
            return false;
        }).orElseGet(() -> {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
            return true;
        });
    }
}
