package com.example.myapp.websocket.chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// REST API 요청을 처리하는 컨트롤러
@RestController
@RequestMapping("/api/chat") //경로
public class MessageSearchController {

    private final MessageSearchService messageSearchService;

    public MessageSearchController(MessageSearchService messageSearchService) {
        this.messageSearchService = messageSearchService;
    }

    // 검색 기능, /search경로로 GET요청 처리
    @GetMapping("/search")
    public List<Message> searchMessages(
            @RequestParam String teamId,    // teamId는 쿼리 파라미터로 받음
            @RequestParam String keyword) { // keyword도 쿼리 파라미터로 받음
        return messageSearchService.searchMessagesByKeyword(teamId, keyword);
    }
}