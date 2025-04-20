package com.example.myapp.IDE.File;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUpdateRequest {

    private Long folderId;
    private Long fileId;
    private String content;
    private String fileName; // 🔹 추가됨
}
