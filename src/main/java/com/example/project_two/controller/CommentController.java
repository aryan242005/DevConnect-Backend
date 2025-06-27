package com.example.project_two.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project_two.dto.CommentRequest;
import com.example.project_two.dto.CommentResponse;
import com.example.project_two.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long postId, @RequestBody CommentRequest commentRequest, Principal principal){
        return new ResponseEntity<>(commentService.addComment(postId, principal.getName(), commentRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable Long commentId){
        log.info("Deleting comment by id : " + commentId);
        return new ResponseEntity<>(commentService.deleteCommentById(commentId), HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsById(@PathVariable Long postId){
        return new ResponseEntity<>(commentService.getCommentsByPost(postId), HttpStatus.OK);
    }
}
