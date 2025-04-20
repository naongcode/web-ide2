package com.example.myapp.IDE.Folder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class FolderCreateResponse {

    private Long folderId;

    private Long teamId;

    private Long questId;

    private String userId;

    private String folderName;

    private Long parentId;

    private Date createdAt;
}
