package com.example.myapp.Membership.controller;


import com.example.myapp.Membership.dto.TeamCreateRequest;
import com.example.myapp.Membership.dto.TeamCreateResponse;
import com.example.myapp.Membership.dto.TeamJoinRequest;
import com.example.myapp.Membership.dto.TeamMemberResponse;
import com.example.myapp.Membership.entity.Team2;
import com.example.myapp.Membership.entity.User2;
import com.example.myapp.Membership.service.TeamService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<?> createTeam(
            @RequestBody TeamCreateRequest request,
            HttpServletRequest httpServletRequest) {

        try {
            // JwtAuthFilter에서 넣어준 userId 꺼냄
            String userId = (String) httpServletRequest.getAttribute("userId");

            Team2 team2 = teamService.createTeam(request, userId);
            // TeamCreateResponse DTO로 변환하여 반환
            TeamCreateResponse response = new TeamCreateResponse(
                    team2.getTeamId(),
                    team2.getTeamName(),
                    team2.getTeamDescription(),
                    team2.getMaxMember(),
                    team2.getCurrentMemberCount(),
                    team2.getLeaderId().getNickname()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("팀 생성 실패: " + e.getMessage());
        }
    }




    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeamInfo(@PathVariable Integer teamId) {
        try {
            Team2 team = teamService.getTeamById(teamId);
            // TeamResponse DTO로 변환하여 반환
            TeamCreateResponse response = new TeamCreateResponse(
                    team.getTeamId(),
                    team.getTeamName(),
                    team.getTeamDescription(),
                    team.getMaxMember(),
                    team.getCurrentMemberCount(),
                    team.getLeaderId().getNickname()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("팀 조회 실패: " + e.getMessage());
        }
    }

    @GetMapping("/{teamId}/member")
    public ResponseEntity<List<TeamMemberResponse>> getTeamMembers(@PathVariable Integer teamId) {
        List<User2> teamMembers = teamService.getTeamMembers(teamId);
        List<TeamMemberResponse> response = teamMembers.stream()
                .map(user -> new TeamMemberResponse(user.getUserId(), user.getNickname()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinTeam(@RequestBody TeamJoinRequest request, HttpServletRequest httpServletRequest) {
        try {
            String userId = (String) httpServletRequest.getAttribute("userId");
            String result = teamService.joinTeam(userId, request.getTeam_id());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("팀 참가 실패: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<TeamCreateResponse>> getTeamsByUserTier(HttpServletRequest request) {
        String userTier = (String) request.getAttribute("tier");
        String currentUserId = (String) request.getAttribute("userId");
        if (userTier != null) {
            userTier = userTier.trim();
        }
        if (currentUserId != null) {
            currentUserId = currentUserId.trim();
        }
        List<TeamCreateResponse> teams = teamService.getTeamsByTier(userTier);
        // 필요하다면 currentUserId를 활용하여 응답 데이터를 추가 처리
        return ResponseEntity.ok(teams);
    }

}
