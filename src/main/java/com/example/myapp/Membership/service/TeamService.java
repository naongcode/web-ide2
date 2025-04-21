package com.example.myapp.Membership.service;

import com.example.myapp.Membership.dto.TeamCreateRequest;
import com.example.myapp.Membership.entity.Team2;
import com.example.myapp.Membership.entity.TeamMember2;
import com.example.myapp.Membership.entity.User2;
import com.example.myapp.Membership.repository.TeamMemberRepository2;
import com.example.myapp.Membership.repository.TeamRepository2;
import com.example.myapp.Membership.repository.UserRepository2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository2 teamRepository;
    private final UserRepository2 userRepository;
    private final TeamMemberRepository2 teamMemberRepository2;  // 추가

    public TeamService(TeamRepository2 teamRepository, UserRepository2 userRepository, TeamMemberRepository2 teamMemberRepository2) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.teamMemberRepository2 = teamMemberRepository2;
    }


    // 팀 생성
    public Team2 createTeam(TeamCreateRequest request, String userId) {
        User2 leader = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("리더 유저를 찾을 수 없습니다."));

        Team2 team2 = Team2.builder()
                .teamName(request.getTeamName())
                .teamDescription(request.getTeamDescription())
                .maxMember(request.getMaxMember())
                .teamTier(request.getTeamTier())
                .leaderId(leader)
                .currentMemberCount(1) // 초기 리더 포함
                .build();

        return teamRepository.save(team2);
    }

    // 팀 상세 조회
    public Team2 getTeamById(Integer teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("해당 팀이 존재하지 않습니다."));
    }


    // 팀원 목록 조회
    public List<User2> getTeamMembers(Integer teamId) {
        Team2 team2 = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("해당 팀이 존재하지 않습니다."));

        return team2.getTeamMembers().stream() // 'teamMembers' 리스트를 사용
                .map(TeamMember2::getUserId)
                .collect(Collectors.toList());
    }

    // 팀 참가
    public String joinTeam(String userId, Integer teamId) {
        if (teamMemberRepository2.existsByUserId_UserIdAndTeamId_TeamId(userId, teamId)) {
            throw new IllegalStateException("이미 참가한 팀입니다.");
        }

        User2 user2 = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Team2 team2 = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

        // 현재 인원 증가
        team2.setCurrentMemberCount(team2.getCurrentMemberCount() + 1);
        teamRepository.save(team2);

        // 팀멤버 등록
        TeamMember2 teamMember2 = new TeamMember2(null, team2, user2);
        teamMemberRepository2.save(teamMember2);

        return "팀 참가 완료";
    }

    public List<Team2> getAllTeams() {
        return teamRepository.findAll();
    }
}