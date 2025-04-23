package com.example.myapp.IDE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class FolderCreateResponse {

    private Long folder_id; //수정(스네이크로)

    private Long team_id; //수정(스네이크로)

    private Long quest_id; //수정(스네이크로)

    private String user_id; //수정(스네이크로)

    private String folder_name; //수정(스네이크로)

    private Long parent_id; //수정(스네이크로)

    private Date createdAt;
}
