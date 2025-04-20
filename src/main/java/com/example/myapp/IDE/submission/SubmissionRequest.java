package com.example.myapp.IDE.submission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmissionRequest {

    private Long questId;        // 어떤 퀘스트에 대한 제출인지
    private String userId;       // 제출한 사용자 ID
    private String codeContext;  // 코드 내용
    private Boolean isCompleted; // 완료 여부
}

