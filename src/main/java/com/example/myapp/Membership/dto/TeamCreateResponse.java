package com.example.myapp.Membership.dto;

import com.example.myapp.Membership.entity.Team2;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamCreateResponse {
    private Integer team_id; //수정(스네이크로)
    private String team_name; //수정(스네이크로)
    private String team_description; //수정(스네이크로)
    private int max_member; //수정(스네이크로)
    private int current_member_count; //수정(스네이크로)
    private String leader_nickname; //수정(스네이크로)

    public TeamCreateResponse(Team2 team, String currentUserId) {
        this.team_id = team.getTeamId();
        this.team_name = team.getTeamName();
        this.team_description = team.getTeamDescription();
        this.max_member = team.getMaxMember();
        this.current_member_count = team.getCurrentMemberCount();

    }

}
