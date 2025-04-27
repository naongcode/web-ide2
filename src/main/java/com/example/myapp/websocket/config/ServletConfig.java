package com.example.myapp.websocket.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.example.myapp.websocket.chat.ChatWebSocketHandler;
import com.example.myapp.websocket.chat.MessageService;
import com.example.myapp.websocket.chat.MessageHistoryService;

@Configuration
public class ServletConfig implements WebSocketConfigurer {

    @Value("${server.port.http}")
    private int serverPortHttp;

    private final MessageService messageService;
    private final MessageHistoryService messageHistoryService;

    public ServletConfig(MessageService messageService, MessageHistoryService messageHistoryService) {
        this.messageService = messageService;
        this.messageHistoryService = messageHistoryService;
    }

    // ServletConfiguration: Tomcat 서버의 HTTP 커넥터 추가
    @Bean
    public ServletWebServerFactory serverFactory() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createHttpConnector()); // HTTP(8080) 추가
        return tomcat;
    }

    private Connector createHttpConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(serverPortHttp);
        connector.setSecure(false);
        connector.setRedirectPort(8443); // HTTP로 들어오면 HTTPS로 리다이렉트하고 싶으면 설정
        return connector;
    }

    // WebSocketConfig: WebSocket 핸들러 등록
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(messageService, messageHistoryService), "/wss/chat")
                .setAllowedOrigins("*");
    }
}