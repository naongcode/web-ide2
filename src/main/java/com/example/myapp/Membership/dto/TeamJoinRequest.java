package com.example.myapp.Membership.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamJoinRequest {
    private String user_id; //수정(스네이크로)
    private Integer team_id; //수정(스네이크로)
}
