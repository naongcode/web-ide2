package com.example.myapp.Membership.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data //수정(추가한거임)
public class TeamCreateRequest {
    private String teamName;
    private String teamDescription;
    private int maxMember;
    private String teamTier;
}
