package com.example.myapp.IDE.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUpdateRequest {

    private Long folder_id; //수정(스네이크로)
    private Long file_id; //수정(스네이크로)
    private String code_context;  //수정 -> codeContext ,초기 context
    private String file_name; // 🔹 추가됨, 수정(스네이크로)
}
