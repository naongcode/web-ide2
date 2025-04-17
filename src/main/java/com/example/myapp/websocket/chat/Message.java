package com.example.myapp.websocket.chat;

import jakarta.persistence.*;
import lombok.*;

// DB 테이블용 JPA 엔티티
@Entity
@Table(name = "chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @Column(name = "chat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "message")
    private String content;

    @Column(name = "created_at")
    private String timestamp;
}