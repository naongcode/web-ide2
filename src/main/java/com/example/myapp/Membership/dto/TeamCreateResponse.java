package com.example.myapp.Membership.dto;

import com.example.myapp.Membership.entity.Team2;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamCreateResponse {
    private Integer teamId;
    private String teamName;
    private String teamDescription;
    private int maxMember;
    private int currentMemberCount;
    private String leaderNickname;

    public TeamCreateResponse(Team2 team, String currentUserId) {
        this.teamId = team.getTeamId();
        this.teamName = team.getTeamName();
        this.teamDescription = team.getTeamDescription();
        this.maxMember = team.getMaxMember();
        this.currentMemberCount = team.getCurrentMemberCount();

    }

}
