package com.example.myapp.question;

import com.example.myapp.IDE.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    boolean existsByTeam_TeamIdAndUser_UserId(Long teamId, String userId);

    // 주어진 teamId에 속하는 모든 TeamMember 조회
    List<TeamMember> findByTeam_TeamId(Long teamId);
}
