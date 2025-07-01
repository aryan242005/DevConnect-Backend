package com.example.project_two.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.project_two.dto.CommentResponse;
import com.example.project_two.dto.PostResponse;
import com.example.project_two.dto.UserResponse;
import com.example.project_two.entity.Comment;
import com.example.project_two.entity.Post;
import com.example.project_two.entity.User;
import com.example.project_two.exception.PostNotFoundException;
import com.example.project_two.exception.UserNotFoundException;
import com.example.project_two.repository.FollowRepository;
import com.example.project_two.repository.LikeRepository;
import com.example.project_two.repository.PostRepository;
import com.example.project_two.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final FollowRepository followRepository;
    private final Cloudinary cloudinary;

    public Post createPost(String postTitle, String postContent, String userEmail, MultipartFile imageFile) throws IOException{
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User not found with email : " + userEmail));
        
        Map<String, Object> options = ObjectUtils.asMap("folder", "post-images");
        String imageUrl = cloudinary.uploader()
            .upload(imageFile.getBytes(), options)
            .get("secure_url").toString();

        Post post = new Post();
        post.setPostTitle(postTitle);
        post.setPostContent(postContent);
        post.setUser(user);
        post.setCreatedAt(java.time.LocalDateTime.now());
        post.setPostImageUrl(imageUrl);

        log.info("Post is being saved to database");
        return postRepository.save(post);
    }

    public List<PostResponse> getAllPosts(String userEmail) {
        List<Post> posts = postRepository.findAll();
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User with email : " + userEmail + " not found"));

        Set<Long> likedPostIds = likeRepository.findLikedPostByUserId(user.getUserId());

        log.info("Mapped posts to dto");
        List<PostResponse> postResponses = posts.stream().map(post -> {
            return mapToDto(post, likedPostIds);
        }).toList();

        return postResponses;
    }

    public List<PostResponse> getAllUserPosts(Long userId){
        List<Post> posts = postRepository.findByUserUserId(userId);

        log.info("Mapped posts to dto");
        List<PostResponse> postResponses = posts.stream().map(post -> {
            return mapToDto(post, null);
        }).toList();

        return postResponses;
    }

    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post with id : " + postId + " not found"));
        
        PostResponse postResponse = mapToDto(post, null);
        List<CommentResponse> commentResponses = post.getComments().stream().map(comment -> {
            return mapToDto(comment);
        }).toList();
        postResponse.setComments(commentResponses);
        return postResponse;
    }
    
    private PostResponse mapToDto(Post post, Set<Long> likedPostIds){
        PostResponse postResponse = new PostResponse();
        
        postResponse.setPostId(post.getPostId());
        postResponse.setPostTitle(post.getPostTitle());
        postResponse.setPostContent(post.getPostContent());
        postResponse.setCreatedAt(post.getCreatedAt());
        postResponse.setPostImageUrl(post.getPostImageUrl());
        postResponse.setUserName(post.getUser().getDisplayName());
        postResponse.setLikeCount(likeRepository.countByPostId(post.getPostId()));
        if(likedPostIds != null) postResponse.setLikedByCurrentUser(likedPostIds.contains(post.getPostId()));
        postResponse.setUserId(post.getUser().getUserId());
        postResponse.setUser(mapToDto(post.getUser(), false));
        
        return postResponse;
    }
    
    public String deletePostById(Long postId) {
        postRepository.deleteById(postId);
        return "Post deleted";
    }
    
    public List<PostResponse> feed(String userEmail, int page, int size){
        User user = userRepository.findByUserEmail(userEmail).orElseThrow();
        List<Long> followingIds = followRepository.findFollowingId(user.getUserId());

        Set<Long> likedPostIds = likeRepository.findLikedPostByUserId(user.getUserId());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findByUserUserIdIn(followingIds, pageable);

        List<PostResponse> postResponses = posts.stream().map(post -> {
            return mapToDto(post, likedPostIds);
        }).toList();

        return postResponses;
    }

    private UserResponse mapToDto(User follower, boolean followedByCurrentUser){
        UserResponse response = new UserResponse();

        response.setUserId(follower.getUserId());
        response.setUserEmail(follower.getUserEmail());
        response.setBio(follower.getBio());
        response.setProfileImageUrl(follower.getProfileImageUrl());
        response.setDisplayName(follower.getDisplayName());
        response.setFollowers(followRepository.findNumFollowers(follower));
        response.setFollowing(followRepository.findNumFollowing(follower));
        if(followedByCurrentUser) response.setFollowedByCurrentUser(followedByCurrentUser);

        return response;
    }

    public CommentResponse mapToDto(Comment comment){
        CommentResponse commentResponse = new CommentResponse();

        commentResponse.setCommentId(comment.getCommentId());
        commentResponse.setCommentContent(comment.getCommentContent());
        commentResponse.setCommentCreatedAt(comment.getCommentCreatedAt());
        commentResponse.setUserResponse(mapToDto(comment.getUser(), false));

        return commentResponse;
    }
}
