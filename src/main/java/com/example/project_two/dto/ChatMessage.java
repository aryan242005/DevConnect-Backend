package com.example.project_two.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name="chats")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long messageId;

    private String senderUsername;
    
    @Column(nullable=true)
    private String receiverUsername;
    
    @Column(nullable=true)
    private String groupName;

    @NotBlank(message="Message cannot be null")
    private String content;
    private LocalDateTime timeStamp;
}
