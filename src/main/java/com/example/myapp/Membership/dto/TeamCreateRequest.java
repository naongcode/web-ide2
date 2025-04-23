package com.example.myapp.Membership.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data //수정(추가한거임)
public class TeamCreateRequest {
    private String team_name; //수정(스네이크로)
    private String team_description; //수정(스네이크로)
    private int max_member; //수정(스네이크로)
    private String team_tier; //수정(스네이크로)
}
