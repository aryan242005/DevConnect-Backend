package com.example.project_two.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project_two.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUserEmail(String userEmail);
    boolean existsByUserEmail(String userEmail);

    @Query("SELECT u from User u WHERE u.userId NOT IN (SELECT f.following.userId FROM Follow f WHERE f.follower.userId = :userId) AND u.userId <> :userId")
    List<User> getRecommendations(Long userId);

    @Query("SELECT imagePublicId from User u WHERE u.userId = :userId")
    String getOldProfileId(Long userId);

    @Modifying
    @Query("UPDATE User u SET u.profileImageUrl = :url, u.imagePublicId = :publicId WHERE u.userId = :userId")
    int updateProfileImageUrl(@Param("userId") Long userId, @Param("url") String url, @Param("publicId") String publicId);

    List<User> findByUserNameContainingIgnoreCase(String searchText);
}   
