package com.example.myapp.websocket.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;


@Getter
@Setter
@NoArgsConstructor
public class MessageRequest {
    @JsonProperty("team_id")
    private Long teamId;
    private String nickname;

    @JsonProperty("user_id")
    private String userId;

    private String content;
    private String type;
}
