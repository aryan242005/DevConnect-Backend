package com.example.project_two.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project_two.dto.CommentRequest;
import com.example.project_two.dto.CommentResponse;
import com.example.project_two.dto.UserResponse;
import com.example.project_two.entity.Comment;
import com.example.project_two.entity.Post;
import com.example.project_two.entity.User;
import com.example.project_two.exception.PostNotFoundException;
import com.example.project_two.exception.UserNotFoundException;
import com.example.project_two.repository.CommentRepository;
import com.example.project_two.repository.PostRepository;
import com.example.project_two.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentResponse addComment(Long postId, String userEmail, CommentRequest commentRequest){
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post with id : " + postId + " not found"));
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User with email : " + userEmail + " not found"));
                                                                                    
        Comment comment = new Comment();

        comment.setCommentContent(commentRequest.getCommentContent());
        comment.setCommentCreatedAt(java.time.LocalDateTime.now());
        comment.setUserName(user.getDisplayName());
        comment.setUser(user);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setCommentId(savedComment.getCommentId());
        commentResponse.setCommentContent(savedComment.getCommentContent());
        commentResponse.setCommentCreatedAt(savedComment.getCommentCreatedAt());
        commentResponse.setUserResponse(mapToDto(savedComment.getUser()));

        return commentResponse;
    }

    public String deleteCommentById(Long commentId) {
        commentRepository.deleteById(commentId);
        return "Comment deleted";
    }

    public List<CommentResponse> getCommentsByPost(Long postId){
        List<Comment> comments = commentRepository.getCommentsByPostId(postId);

        List<CommentResponse> commentResponses = comments.stream().map(comment -> {
            return mapToDto(comment);
        }).toList();

        return commentResponses;
    }

    public CommentResponse mapToDto(Comment comment){
        CommentResponse commentResponse = new CommentResponse();

        commentResponse.setCommentId(comment.getCommentId());
        commentResponse.setCommentContent(comment.getCommentContent());
        commentResponse.setCommentCreatedAt(comment.getCommentCreatedAt());
        commentResponse.setUserResponse(mapToDto(comment.getUser()));

        return commentResponse;
    }

    private UserResponse mapToDto(User user){
        UserResponse response = new UserResponse();

        response.setUserId(user.getUserId());
        response.setUserEmail(user.getUserEmail());
        response.setBio(user.getBio());
        response.setProfileImageUrl(user.getProfileImageUrl());
        response.setDisplayName(user.getDisplayName());

        return response;
    }
}
