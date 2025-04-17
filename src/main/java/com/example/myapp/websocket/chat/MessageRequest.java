package com.example.myapp.websocket.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageRequest {
    private String teamId;
    private String nickname;
    private String userId;
    private String content;
    // 메세지 타입
    private String type;
}
