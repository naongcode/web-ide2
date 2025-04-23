package com.example.myapp.IDE.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class FileCreateResponse {

    private Long fileId;
    private Long folderId;
    private String fileName;     // 추가됨
    private String language;
    private Long teamId;
    private Long questId;
    private String userId;
    private Date createdAt;

    private Long submitId;

}
