package com.example.project_two.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_two.dto.ChatMessage;

public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySenderUsernameAndReceiverUsernameOrReceiverUsernameAndSenderUsername(String sender1, String receiver1, String sender2, String receiver2);

}
