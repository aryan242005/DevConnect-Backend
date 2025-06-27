package com.example.project_two.configuration;

import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.project_two.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if(token != null) token = token.substring(7);
            if (token != null && jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUserName(token);
                Authentication auth = new UsernamePasswordAuthenticationToken(username, null, List.of()); // add roles if needed

                accessor.setUser(auth);
            } else {
                throw new IllegalArgumentException("Invalid JWT token");
            }
        }
        return message;
    }
}
