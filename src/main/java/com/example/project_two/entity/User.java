package com.example.project_two.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.validator.constraints.URL;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name="users")
public class User implements UserDetails{
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    Long userId;

    @NotBlank(message="Username cannot be blank")
    @Size(min=3, max=30,  message = "User name must be 3-30 characters")
    private String userName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String userEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String userPassword;

    @Size(max = 200, message = "Bio can't exceed 200 characters")
    @Column(nullable=false)
    private String bio;

    @URL(message = "Profile image URL must be valid")
    private String profileImageUrl;
    
    @Enumerated(EnumType.STRING)
    private Role userRole;
    private LocalDateTime createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authority = "ROLE_" + userRole.name();
        System.out.println(authority);
        return List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
    }
    @Override
    public String getPassword() {
        return this.userPassword;
    }
    @Override
    public String getUsername() {
        return this.userEmail;
    }

    public String getDisplayName(){
        return this.userName;
    }

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JsonIgnore
    @JsonManagedReference
    @ToString.Exclude
    private List<Post> posts;

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Comment> comments;

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Like> likes;

    @Column(nullable=true)
    private String imagePublicId;

}
