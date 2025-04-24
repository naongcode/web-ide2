package com.example.myapp.IDE.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class SubmissionResponse {
    private Integer submission_id; //수정(스네이크로)
    private String output;
    private Date submittedAt;
//    private String language;
}
