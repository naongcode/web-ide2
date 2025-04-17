package com.example.myapp.websocket.chat;

import com.example.myapp.websocket.util.DateFormatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.util.Collections;

import static org.mockito.Mockito.*;

class ChatWebSocketHandlerTest {

    private ChatWebSocketHandler handler;
    private MessageService messageService;
    private MessageHistoryService messageHistoryService;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        messageService = mock(MessageService.class);
        messageHistoryService = mock(MessageHistoryService.class);
        handler = new ChatWebSocketHandler(messageService, messageHistoryService);
        mapper = new ObjectMapper();
    }

    @Test
    void handleTextMessage_정상작동() throws Exception {
        // given
        WebSocketSession session = mock(WebSocketSession.class);

        MessageRequest request = new MessageRequest();
        request.setNickname("테스터");
        request.setTeamId("team1");
        request.setUserId("user123");
        request.setContent("안녕하세요");

        String payload = mapper.writeValueAsString(request);
        TextMessage textMessage = new TextMessage(payload);

        Message pastMessage = Message.builder()
                .nickname("이전유저")
                .teamId("team1")
                .userId("user999")
                .content("이전 메시지")
                .timestamp(DateFormatUtil.convertToMySQLDateFormat(Instant.now()))
                .build();

        when(messageHistoryService.getMessageHistoryByTeamId("team1"))
                .thenReturn(Collections.singletonList(pastMessage));

        // when
        handler.handleTextMessage(session, textMessage);

        // then
        verify(messageService).saveMessage(any(MessageRequest.class)); // 저장 호출 확인
        verify(session, atLeastOnce()).sendMessage(any(TextMessage.class)); // 응답 메시지 전송 확인
    }
}
