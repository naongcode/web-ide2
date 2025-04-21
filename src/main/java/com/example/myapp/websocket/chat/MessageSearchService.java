package com.example.myapp.websocket.chat;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageSearchService {

    private final MessageRepository messageRepository;

    public MessageSearchService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> searchMessagesByKeyword(Long teamId, String keyword) {
        return messageRepository.findByTeamIdAndContentContaining(teamId, keyword);
    }
}