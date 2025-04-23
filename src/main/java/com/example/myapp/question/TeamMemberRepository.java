package com.example.myapp.question;

import com.example.myapp.IDE.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    boolean existsByTeam_TeamIdAndUser_UserId(Long teamId, String userId);
}
