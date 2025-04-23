package com.example.myapp.IDE.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class FileCreateResponse {

    private Long file_id; //수정(스네이크로)
    private Long folder_id; //수정(스네이크로)
    private String file_name;     // 추가됨,수정(스네이크로)
    private String language;
    private Long team_id; //수정(스네이크로)
    private Long quest_id; //수정(스네이크로)
    private String user_id; //수정(스네이크로)
    private Date createdAt;

    private Long submission_id; //수정(스네이크로), 수정전(submitId)

}
