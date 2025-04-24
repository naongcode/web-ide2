package com.example.myapp.Membership.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberResponse {
    private String user_id;
    private String nickname;
}
