package com.example.myapp.websocket.chat;

import com.example.myapp.websocket.util.DateFormatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Instant;
import java.util.List;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private final MessageService messageService;
    private final MessageHistoryService messageHistoryService;

    // 클라이언트로부터 받은 teamId를 저장할 변수
    private Long teamId;

    public ChatWebSocketHandler(MessageService messageService, MessageHistoryService messageHistoryService) {
        this.messageService = messageService;
        this.messageHistoryService = messageHistoryService;
    }

    // 이전 메세지 보여주기
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println("✅ WebSocket 연결됨: " + session.getId());

        // 첫 번째 메시지가 오기 전에 teamId를 초기화하지 않음
        // 팀 ID는 클라이언트에서 첫 번째 메시지를 보낼 때 설정됨
    }

    // 메세지 송신/수신
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // session은 연결을 나타내는 객체, message가 수신한 텍스트
        // JSON 메세지를 java 객체로 변환
        String payload = message.getPayload();
        System.out.println("📥 수신된 메시지 (raw): " + payload);

        MessageRequest request = mapper.readValue(payload, MessageRequest.class);

        try {
            System.out.println("닉네임: " + request.getNickname());
            System.out.println("내용: " + request.getContent());

            // 클라이언트에서 teamId를 처음 전송하는 경우 teamId 설정
            if (teamId == null && request.getTeamId() != null) {
                teamId = request.getTeamId();
            }

            // teamId가 설정되지 않았을 경우 예외 처리 (혹은 기본값 설정)
            if (teamId == null) {
                session.sendMessage(new TextMessage("teamId가 설정되지 않았습니다."));
                return;
            }

            // teamId로 메시지 조회
            List<Message> messages = messageHistoryService.getMessageHistoryByTeamId(teamId);

            // 조회된 메시지를 클라이언트에게 전송
            for (Message msg : messages) {
                MessageResponse response = new MessageResponse(
                        msg.getTeamId(),
                        msg.getNickname(),
                        msg.getUserId(),
                        msg.getContent(),
                        msg.getTimestamp()
                );

                String json = mapper.writeValueAsString(response);
                session.sendMessage(new TextMessage(json));
            }

            // 메시지 저장
            messageService.saveMessage(request);

            // 응답 객체 생성
            MessageResponse response = new MessageResponse(
                    request.getTeamId(),
                    request.getNickname(),
                    request.getUserId(),
                    request.getContent(),
                    DateFormatUtil.convertToMySQLDateFormat(Instant.now())  // MySQL 형식으로 변환
            );

            // 객체를 JSON 문자열로 변환. json에 저장
            String json = mapper.writeValueAsString(response);
            // 클라이언트에 메세지 보냄
            session.sendMessage(new TextMessage(json));
        } catch (Exception e) {
            // 예외 발생 시 에러 로그 출력
            System.err.println("메시지 저장 실패: " + e.getMessage());

            // 클라이언트에 실패 메시지 전송
            String errorMessage = "메시지 전송 실패. 다시 시도해 주세요.";
            session.sendMessage(new TextMessage(errorMessage));
        }
    }
}