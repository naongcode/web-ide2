package com.example.myapp.repository;

import com.example.myapp.IDE.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
    // Quest의 questId와 User의 userId를 이용해 Submission을 찾는 메서드
    Optional<Submission> findByQuest_questIdAndUser_UserId(Long questId, String userId);

    // 특정 문제에 대해 유저가 제출했는지 여부 확인
    boolean existsByQuest_QuestIdAndUser_UserId(Long questId, String userId);

}