package com.example.myapp.repository;

import com.example.myapp.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    // Quest의 questId와 User의 userId를 이용해 Submission을 찾는 메서드
    Optional<Submission> findByQuest_questIdAndUser_UserId(Long questId, String userId);

}