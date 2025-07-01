package com.example.project_two.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import com.example.project_two.dto.ChatMessage;
import com.example.project_two.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRepository chatRepository;

    @PreAuthorize("hasRole('USER')")
    @MessageMapping("/chat")
    public void handleChat(@Payload ChatMessage chatMessage) {
        chatMessage.setTimeStamp(LocalDateTime.now());
        chatRepository.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getReceiverUsername(),
                "/queue/messages",
                chatMessage
        );

        messagingTemplate.convertAndSendToUser(
                chatMessage.getSenderUsername(),
                "/queue/messages",
                chatMessage
        );
    }
}
