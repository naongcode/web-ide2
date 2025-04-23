package com.example.myapp.IDE.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class SubmissionResponse {
    private Integer submissionId;
    private String output;
    private Date submittedAt;
    private String language;
}
