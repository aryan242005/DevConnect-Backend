package com.example.project_two.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project_two.dto.ChatMessage;
import com.example.project_two.service.MessageService;

import lombok.RequiredArgsConstructor;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/{receiverUserName}")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable String receiverUserName,Principal principal){
        return new ResponseEntity<>(messageService.getMessages(principal.getName(), receiverUserName), HttpStatus.OK);
    }
}
