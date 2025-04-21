package com.example.myapp.websocket.chat;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageHistoryService {

    private final MessageRepository messageRepository;

    public MessageHistoryService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // 팀 ID에 따라 이전 메시지 목록을 가져오는 메서드
    public List<Message> getMessageHistoryByTeamId(Long teamId) {
        return messageRepository.findAllByTeamId(teamId);
    }

    // 검색 기능 추가 (예: 내용으로 검색)
    public List<Message> searchMessagesByKeyword(Long teamId, String keyword) {
        return messageRepository.findByTeamIdAndContentContaining(teamId, keyword);
    }
}