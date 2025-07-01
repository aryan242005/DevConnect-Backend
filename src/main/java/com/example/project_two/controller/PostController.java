package com.example.project_two.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.project_two.dto.PostResponse;
import com.example.project_two.entity.Post;
import com.example.project_two.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping(path="/create", consumes="multipart/form-data")
    public ResponseEntity<Post> createPost(
        @Valid 
        @RequestParam("postTitle") String postTitle, 
        @RequestParam("postContent") String postContent, 
        @RequestParam(value="postImage", required=false) MultipartFile postImage,
        Principal principal) throws IOException {
        log.info("Post created");
        return new ResponseEntity<>(postService.createPost(postTitle, postContent, principal.getName(), postImage), HttpStatus.OK);
    }
    
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/all")
    public ResponseEntity<List<PostResponse>> getAllPosts(Principal principal){
        log.info("Fetching all posts");
        return new ResponseEntity<>(postService.getAllPosts(principal.getName()), HttpStatus.OK);
    }
    
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<PostResponse>> getAllUserPosts(Long userId){
        log.info("Fetching all user posts");
        return new ResponseEntity<>(postService.getAllUserPosts(userId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/postId/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long postId){
        log.info("Fetching post by id : " + postId);
        return new ResponseEntity<>(postService.getPostById(postId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePostById(@PathVariable Long postId){
        log.info("Deleting post by id : " + postId);
        return new ResponseEntity<>(postService.deletePostById(postId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> feed(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="1") int size, Principal principal){
        log.info("Getting posts for feed");
        return new ResponseEntity<>(postService.feed(principal.getName(), page, size), HttpStatus.OK);
    }
}
