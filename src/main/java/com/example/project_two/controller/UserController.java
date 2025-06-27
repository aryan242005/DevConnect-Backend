package com.example.project_two.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.project_two.dto.LoginRequest;
import com.example.project_two.dto.LoginResponse;
import com.example.project_two.dto.UserResponse;
import com.example.project_two.entity.User;
import com.example.project_two.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = userService.login(loginRequest);
        log.info("User logged in");
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged Out");
    }
    
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        if(user != null){
            log.info("User created");
            return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/current-user")
    public ResponseEntity<UserResponse> getCurrentUser(Principal principal){
        return new ResponseEntity<>(userService.getCurrentUser(principal.getName()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId, Principal principal){
        return new ResponseEntity<>(userService.getUserById(userId, principal.getName()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/follow/{followingId}/{followedByCurrentUser}")
    public ResponseEntity<Boolean> toggleFollow(@PathVariable Long followingId, @PathVariable boolean followedByCurrentUser, Principal principal){
        return new ResponseEntity<>(userService.toggleFollow(principal.getName(), followingId, followedByCurrentUser), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/follow/followers")
    public ResponseEntity<List<UserResponse>> getAllFollowers(Principal principal){
        return new ResponseEntity<>(userService.getAllFollowers(principal.getName()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/follow/following")
    public ResponseEntity<List<UserResponse>> getAllFollowing(Principal principal){
        return new ResponseEntity<>(userService.getAllFollowing(principal.getName()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<List<UserResponse>> getRecommendations(@PathVariable Long userId){
        return new ResponseEntity<>(userService.getRecommendations(userId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(path="/profile/{userId}", consumes="multipart/form-data")
    public ResponseEntity<Boolean> changeProfileImage(@RequestParam("profileImage") MultipartFile profileImage, @PathVariable("userId") Long userId) throws IOException{
        return new ResponseEntity<>(userService.changeProfileImage(profileImage, userId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search/{searchText}")
    public ResponseEntity<List<UserResponse>> searchUsers(@PathVariable("searchText") String searchText){
        return new ResponseEntity<>(userService.searchUsers(searchText), HttpStatus.OK);
    }
}