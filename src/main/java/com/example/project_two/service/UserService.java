package com.example.project_two.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.project_two.dto.LoginRequest;
import com.example.project_two.dto.LoginResponse;
import com.example.project_two.dto.UserResponse;
import com.example.project_two.entity.Follow;
import com.example.project_two.entity.Role;
import com.example.project_two.entity.User;
import com.example.project_two.exception.DuplicateUserException;
import com.example.project_two.exception.InvalidInputException;
import com.example.project_two.exception.UserNotFoundException;
import com.example.project_two.repository.FollowRepository;
import com.example.project_two.repository.UserRepository;
import com.example.project_two.util.JwtUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final Cloudinary cloudinary;
    @PersistenceContext
    private EntityManager entityManager;

    public User createUser(User user){
        if (user.getUserEmail() == null || user.getUserPassword() == null) {
            log.error("Invalid input for user: {}", user.getUserEmail());
            throw new InvalidInputException("Invalid user details");
        }

        if(userRepository.existsByUserEmail(user.getUserEmail())){
            log.error("Duplicate user found for email: {}", user.getUserEmail());
            throw new DuplicateUserException("User with email " + user.getUserEmail() + " already exists");
        }

        log.info("Saving user to the database");
        user.setUserPassword(encoder.encode(user.getUserPassword()));
        user.setUserRole(Role.USER);
        User savedUser = userRepository.save(user);

        log.info("Sending registration email");
        emailService.sendEmail(user.getUserEmail(), "Welcome to DevConnect", "Hi " + user.getDisplayName() + ",\n\nThanks for registering with us!");

        return savedUser;
    }

    public List<UserResponse> getAllUsers(){
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();

        List<UserResponse> userResponses = users.stream().map(user -> {
            return mapToDto(user, false, false);
        }).toList();

        return userResponses;
    }

    public UserResponse getCurrentUser(String userEmail){
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User with email: " + userEmail + " not found"));
        return mapToDto(user, false, false);
    }

    public UserResponse getUserById(Long userId, String userEmail){
        User currentUser = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));
        boolean followedByCurrentUser = followRepository.existsByFollower_UserIdAndFollowingUserId(currentUser.getUserId(), userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));
        return mapToDto(user, followedByCurrentUser, true);
    }

    public LoginResponse login(LoginRequest loginRequest){
        try{
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserEmail(), loginRequest.getUserPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            
            return new LoginResponse(token, userDetails.getUsername(), userDetails.getAuthorities().toString());
        }
        catch(AuthenticationException e){
            throw new InvalidInputException("Invalid email or password");
        }
    }

    @Transactional
    public boolean toggleFollow(String userEmail, Long followingId, boolean followedByCurrentUser){
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User with email: " + userEmail + " not found"));
        if(followedByCurrentUser){
            followRepository.deleteByFollower_UserIdAndFollowing_UserId(user.getUserId(), followingId);
            return false;
        }else{
            Follow follow = new Follow();
            follow.setFollower(entityManager.getReference(User.class, user.getUserId()));
            follow.setFollowing(entityManager.getReference(User.class, followingId));
            followRepository.save(follow);
            return true;
        }
    }

    @Transactional(readOnly=true)
    public List<UserResponse> getAllFollowers(String userEmail){
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User with email: " + userEmail + " not found"));
        List<User> followers = followRepository.findFollowers(user);

        List<UserResponse> followersResponse = followers.stream().map(follower -> {
            return mapToDto(follower, false, false);
        }).toList();

        return followersResponse;
    }

    @Transactional(readOnly=true)
    public List<UserResponse> getAllFollowing(String userEmail){
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User with email: " + userEmail + " not found"));
        List<User> following = followRepository.findFollowing(user);

        List<UserResponse> followingResponse = following.stream().map(follow -> {
            return mapToDto(follow, false, false);
        }).toList();

        return followingResponse;
    }
    
    public List<UserResponse> getRecommendations(Long userId){
        List<User> users = userRepository.getRecommendations(userId);

        List<UserResponse> userResponses = users.stream().map(user -> {
            return mapToDto(user, false, false);
        }).toList();

        return userResponses;
    }

    @Transactional
    public boolean changeProfileImage(MultipartFile imageFile, Long userId) throws IOException{
        String oldPublicId = userRepository.getOldProfileId(userId);
        if(oldPublicId != null) cloudinary.uploader().destroy(oldPublicId, ObjectUtils.emptyMap());

        Map<String, Object> options = ObjectUtils.asMap("folder", "profile-images");
        Map<String, String> uploadResult = cloudinary.uploader()
            .upload(imageFile.getBytes(), options);

        String imageUrl = uploadResult.get("secure_url");
        String newPublicId = uploadResult.get("public_id");

        int updated = userRepository.updateProfileImageUrl(userId, imageUrl, newPublicId);
        return updated > 0;
    }

    public List<UserResponse> searchUsers(String searchText){
        List<User> searchUsers = userRepository.findByUserNameContainingIgnoreCase(searchText);

        List<UserResponse> userResponses = searchUsers.stream().map(searchUser -> {
            return mapToDto(searchUser, false, false);
        }).toList();

        return userResponses;
    }

    private UserResponse mapToDto(User follower, boolean followedByCurrentUser, boolean fetchPosts){
        UserResponse response = new UserResponse();

        response.setUserId(follower.getUserId());
        response.setUserEmail(follower.getUserEmail());
        response.setBio(follower.getBio());
        response.setProfileImageUrl(follower.getProfileImageUrl());
        response.setDisplayName(follower.getDisplayName());
        response.setFollowers(followRepository.findNumFollowers(follower));
        response.setFollowing(followRepository.findNumFollowing(follower));
        if(followedByCurrentUser) response.setFollowedByCurrentUser(followedByCurrentUser);
        if(fetchPosts) response.setPosts(follower.getPosts());

        return response;
    }
}