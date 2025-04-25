package com.example.myapp.Membership.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private String nickname;
    private String email;
    private String tier;
    private Integer team_id; //수정(스네이크로)
    private String user_id;

}
