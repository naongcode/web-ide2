package com.example.myapp.question;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // teamId로 문제 목록을 조회
    List<Question> findByTeamId(Long teamId);
}