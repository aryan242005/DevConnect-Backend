package com.example.project_two.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.project_two.entity.Follow;
import com.example.project_two.entity.User;

public interface FollowRepository extends JpaRepository<Follow, Long>{
    boolean existsByFollower_UserIdAndFollowingUserId(Long followerId, Long followingId);

    void deleteByFollower_UserIdAndFollowing_UserId(Long followerId, Long followingId);

    @Query("SELECT f.follower FROM Follow f WHERE f.following = :user")
    List<User> findFollowers(User user);

    @Query("SELECT f.following FROM Follow f WHERE f.follower = :user")
    List<User> findFollowing(User user);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following = :user")
    int findNumFollowers(User user);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower = :user")
    int findNumFollowing(User user);
    
    @Query("SELECT f.following.userId FROM Follow f WHERE f.follower.userId = :userId")
    List<Long> findFollowingId(Long userId);
}
