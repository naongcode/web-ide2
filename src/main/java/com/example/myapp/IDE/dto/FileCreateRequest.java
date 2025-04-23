package com.example.myapp.IDE.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileCreateRequest {

    private Long folder_id;       // null 가능 (최상위 폴더), 수정(스네이크로)
    private String file_name;     // 실제 구현 시, 파일명 따로 저장할 수도 있음수정(스네이크로)
    private String language;

    private Long team_id; //수정(스네이크로)
    private Long quest_id; //수정(스네이크로)
    private String user_id; //수정(스네이크로)

}
