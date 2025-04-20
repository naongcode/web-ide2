package com.example.myapp.repository;

import com.example.myapp.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestRepository extends JpaRepository<Quest, Long> {
    // 필요에 따라 추가적인 쿼리 메서드를 정의할 수 있습니다.
}