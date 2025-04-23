package com.example.myapp.IDE.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileInfo {
    private Long file_id;
    private String file_name;
    private String code_context;
    private String language;
}