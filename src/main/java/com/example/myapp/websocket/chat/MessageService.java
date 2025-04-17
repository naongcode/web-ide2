package com.example.myapp.websocket.chat;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.myapp.websocket.util.DateFormatUtil;

import java.time.Instant;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(MessageRequest request) {
        // 날짜 변환용
        String formattedTimestamp = DateFormatUtil.convertToMySQLDateFormat(Instant.now());


        Message message = Message.builder()
                .teamId(request.getTeamId())
                .nickname(request.getNickname())
                .userId(request.getUserId())
                .content(request.getContent())
                .timestamp(formattedTimestamp)  // 변환된 날짜 형식 사용
                .build();

        messageRepository.save(message);
    }
}
