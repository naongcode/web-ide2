package com.example.myapp.Membership.repository;

import com.example.myapp.Membership.entity.TeamMember2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamMemberRepository2 extends JpaRepository<TeamMember2, Integer> {
    //유저가 해당팀에 속했는지 확인
    Optional<TeamMember2> findByUserId_UserId(String userId);

    // 유저가 특정 팀에 이미 가입했는지 확인(명칭 수정)
    boolean existsByUserId_UserIdAndTeamId_TeamId(String userId, Integer teamId);



}