package com.example.myapp.websocket.chat;

import com.example.myapp.websocket.util.DateFormatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.example.myapp.Membership.util.ExtractInfoFromToken;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    private final MessageService messageService;
    private final MessageHistoryService messageHistoryService;

    // 브로드캐스트용
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    // 수동으로 객체 생성
    private final ExtractInfoFromToken extractInfoFromToken = new ExtractInfoFromToken();

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
        sessions.add(session); // ✅ 연결된 세션 추가
        System.out.println("✅ WebSocket 연결됨: " + session.getId());

        // 첫 번째 메시지가 오기 전에 teamId를 초기화하지 않음
        // 팀 ID는 클라이언트에서 첫 번째 메시지를 보낼 때 설정됨
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session); // ✅ 연결 종료된 세션 제거
        System.out.println("❌ WebSocket 연결 해제됨: " + session.getId());
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

            // 토큰에서 유저 ID 추출 (Authorization 헤더에서 Bearer 토큰 추출)
//            List<String> authHeaders = session.getHandshakeHeaders().get("Authorization");
//            if (authHeaders == null || authHeaders.isEmpty()) {
//                System.out.println("❌ Authorization 헤더가 없습니다.");
//                session.sendMessage(new TextMessage("인증 정보가 없습니다."));
//                return;
//            }

//            String token = authHeaders.get(0).replace("Bearer ", "");
//            String userId = extractInfoFromToken.extractUserIdFromToken(token.replace("Bearer ", ""));

            switch (request.getType()) {
                case "message":
                    handleChatMessage(session, request);
                    break;

//                case "history":
//                    handleHistoryRequest(session, request.getTeamId());
//                    break;
//
//                case "join":
//                    handleJoinMessage(session, request.getTeamId(), request.getNickname(), userId);
//                    break;

                default:
                    session.sendMessage(new TextMessage("❌ 타입:message,history,join"));
            }
        } catch (Exception e) {
            // 예외 발생 시 에러 로그 출력
            System.err.println("메시지 저장 실패: " + e.getMessage());

            // 클라이언트에 실패 메시지 전송
            String errorMessage = "전송실패. team_id,nickname,content";
            session.sendMessage(new TextMessage(errorMessage));
        }
    }


//    private void handleHistoryRequest(WebSocketSession session, Long teamId) throws Exception {
//        List<Message> messages = messageHistoryService.getMessageHistoryByTeamId(teamId);
//
//        for (Message msg : messages) {
//            MessageResponse response = new MessageResponse(
//                    msg.getTeamId(),
//                    msg.getNickname(),
//                    msg.getUserId(),
//                    msg.getContent(),
//                    msg.getTimestamp()
//            );
//
//            String json = mapper.writeValueAsString(response);
//            session.sendMessage(new TextMessage(json));
//        }
//    }

//    private void handleJoinMessage(WebSocketSession session, Long teamId, String nickname, String userId) throws Exception {
//        String joinMsg = nickname + "님이 입장하셨습니다.";
//
//        MessageResponse response = new MessageResponse(
//                teamId,
//                nickname,
//                userId,
//                joinMsg,
//                DateFormatUtil.convertToMySQLDateFormat(Instant.now())
//        );
//
//        String json = mapper.writeValueAsString(response);
//        session.sendMessage(new TextMessage(json));
//    }

    private void handleChatMessage(WebSocketSession session, MessageRequest request) throws Exception {
        // 메시지 저장
        messageService.saveMessage(request);

        // 응답 객체 생성
        MessageChatResponse response = new MessageChatResponse(
                request.getTeamId(),
                request.getNickname(),
//                userId,
                request.getContent(),
                DateFormatUtil.convertToMySQLDateFormat(Instant.now()),
                request.getType()
        );

        String json = mapper.writeValueAsString(response);
        TextMessage textMessage = new TextMessage(json);

        // 모든 세션에 메세지 전파
        for (WebSocketSession clientSession : sessions) {
            if (clientSession.isOpen()) {
                clientSession.sendMessage(textMessage);
            }
        }
    }
}