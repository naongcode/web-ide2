package com.example.myapp.IDE.File;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileCreateRequest {

    private Long folderId;       // null 가능 (최상위 폴더)
    private String fileName;     // 실제 구현 시, 파일명 따로 저장할 수도 있음
    private String language;

    private Long teamId;
    private Long questId;
    private String userId;

}
