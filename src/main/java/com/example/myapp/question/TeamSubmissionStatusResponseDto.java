package com.example.myapp.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TeamSubmissionStatusResponseDto {
    private Long teamId;
    private Long questId;
    private List<UserSubmissionStatusDto> submissions;
}