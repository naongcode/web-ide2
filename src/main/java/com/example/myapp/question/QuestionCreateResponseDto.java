package com.example.myapp.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class QuestionCreateResponseDto {
    private boolean success;
    private String message;
}
