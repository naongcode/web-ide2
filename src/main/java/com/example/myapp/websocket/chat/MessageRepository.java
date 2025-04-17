package com.example.myapp.websocket.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByTeamId(String teamId); // teamId별 메시지 조회
    List<Message> findByTeamIdAndContentContaining(String teamId, String keyword);

}