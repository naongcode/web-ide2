package com.example.myapp.websocket.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.example.myapp.websocket.chat.ChatWebSocketHandler;
import com.example.myapp.websocket.chat.MessageService;
import com.example.myapp.websocket.chat.MessageHistoryService;

@Configuration
public class AppConfig implements WebSocketConfigurer {

    @Value("${server.port.http}")
    private int serverPortHttp;

    private final MessageService messageService;
    private final MessageHistoryService messageHistoryService;

    public AppConfig(MessageService messageService, MessageHistoryService messageHistoryService) {
        this.messageService = messageService;
        this.messageHistoryService = messageHistoryService;
    }

    // ServletConfiguration: Tomcat 서버의 HTTP 커넥터 추가
    @Bean
    public ServletWebServerFactory serverFactory() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcat;
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(serverPortHttp);
        return connector;
    }

    // WebSocketConfig: WebSocket 핸들러 등록
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(messageService, messageHistoryService), "/wss/chat")
                .setAllowedOrigins("*");
    }
}