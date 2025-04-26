package com.example.myapp.websocket.chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// REST API 요청을 처리하는 컨트롤러
@RestController
@RequestMapping("/chat")
public class MessageSearchController {

    private final MessageSearchService messageSearchService;
    private final MessageHistoryService messageHistoryService; // 🔹 추가된 필드

    // 🔧 생성자에 MessageHistoryService 추가
    public MessageSearchController(MessageSearchService messageSearchService,
                                   MessageHistoryService messageHistoryService) {
        this.messageSearchService = messageSearchService;
        this.messageHistoryService = messageHistoryService;
    }

    // 🔍 검색 기능
    @GetMapping("/search")
    public List<Message> searchMessages(
            @RequestParam Long teamId,
            @RequestParam String keyword) {
        return messageSearchService.searchMessagesByKeyword(teamId, keyword);
    }

    // 📜 전체 메시지 히스토리 조회
    @GetMapping("/history")
    public List<Message> getAllMessages(
            @RequestParam Long teamId) {
        return messageHistoryService.getMessageHistoryByTeamId(teamId);
    }
}