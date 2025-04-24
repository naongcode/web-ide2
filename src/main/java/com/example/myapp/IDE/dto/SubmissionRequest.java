package com.example.myapp.IDE.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmissionRequest {

    @JsonProperty("quest_id")  // 스네이크 케이스와 매핑
    private Long questId;

    @JsonProperty("user_id")  // 스네이크 케이스와 매핑
    private String userId;

//    @JsonProperty("code_context")  // 스네이크 케이스와 매핑
//    private String codeContext;

    @JsonProperty("is_completed")  // 스네이크 케이스와 매핑
    private Boolean isCompleted;

//    private String language;
}

