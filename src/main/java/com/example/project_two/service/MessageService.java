package com.example.project_two.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project_two.dto.ChatMessage;
import com.example.project_two.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatRepository chatRepository;

    public List<ChatMessage> getMessages(String user1, String user2) {
        return chatRepository.findBySenderUsernameAndReceiverUsernameOrReceiverUsernameAndSenderUsername(user1, user2, user1, user2);
    }
}
