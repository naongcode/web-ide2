package com.example.myapp.websocket.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageResponse {
    private Long teamId;
    private String nickname;
//    private String userId;
    private String content;
    private String timestamp;

}