package com.example.myapp.websocket.config;

import com.example.myapp.websocket.chat.ChatWebSocketHandler;
import com.example.myapp.websocket.chat.MessageService;
import com.example.myapp.websocket.chat.MessageHistoryService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MessageService messageService;
    private final MessageHistoryService messageHistoryService;

    public WebSocketConfig(MessageService messageService, MessageHistoryService messageHistoryService) {
        this.messageService = messageService;
        this.messageHistoryService = messageHistoryService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // `ChatWebSocketHandler`에 `MessageService`와 `MessageHistoryService`를 주입하여 등록
        registry.addHandler(new ChatWebSocketHandler(messageService, messageHistoryService), "/wss/chat")
                .setAllowedOrigins("*");
    }
}

// ChatWebSocketHandler를 /ws/chat엔드포인트에 매핑
// ChatWebSocketHandler는 메세지 송수신, 연결/종료 담당