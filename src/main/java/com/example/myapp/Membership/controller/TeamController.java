package com.example.myapp.Membership.controller;


import com.example.myapp.Membership.dto.TeamCreateRequest;
import com.example.myapp.Membership.dto.TeamCreateResponse;
import com.example.myapp.Membership.dto.TeamJoinRequest;
import com.example.myapp.Membership.entity.Team2;
import com.example.myapp.Membership.entity.User2;
import com.example.myapp.Membership.service.TeamService;
import com.example.myapp.Membership.util.extractInfoFromToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                    team2.getLeaderId().getUserId()
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
            return ResponseEntity.ok(team);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("팀 조회 실패: " + e.getMessage());
        }
    }

    @GetMapping("/{teamId}/member")
    public ResponseEntity<?> getTeamMembers(@PathVariable Integer teamId) {
        try {
            List<User2> members = teamService.getTeamMembers(teamId);
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("팀원 조회 실패: " + e.getMessage());
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinTeam(@RequestBody TeamJoinRequest request) {
        try {
            String result = teamService.joinTeam(request.getUser_id(), request.getTeam_id()); //수정(스네이크로)
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("팀 참가 실패: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllTeams(@RequestHeader("Authorization") String token) {
        try {
            String userId = extractInfoFromToken.extractUserIdFromToken(token);
            List<TeamCreateResponse> teams = teamService.getAllTeams(userId);
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("팀 리스트 조회 실패: " + e.getMessage());
        }
    }

}
