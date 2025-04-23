package com.example.myapp.question;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionAccessResponse {
    private Long quest_id; //수정(스네이크로)
    private String quest_name; //수정(스네이크로)
    private String quest_link; //수정(스네이크로)
    private boolean accessible;
}
