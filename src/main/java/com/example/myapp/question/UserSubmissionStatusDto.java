package com.example.myapp.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserSubmissionStatusDto {
    private String userId;
    private String nickname;
    private Boolean isCompleted; // 제출 안했으면 null or false 처리
}