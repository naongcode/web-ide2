package com.example.myapp.question;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionAccessResponse {
    private Long questId;
    private String questName;
    private String questLink;
    private boolean accessible;
}
