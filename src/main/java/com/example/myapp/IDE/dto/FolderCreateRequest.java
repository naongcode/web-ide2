package com.example.myapp.IDE.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderCreateRequest {

    private Long team_id; //수정(스네이크로)

    private Long quest_id; //수정(스네이크로)

    private String user_id; //수정(스네이크로)

    private Long parent_id; // null이면 최상위 폴더,수정(스네이크로)

//    private String name; // 폴더 이름
//    @JsonProperty("folder_name")
    private String folder_name; // 폴더 이름, 수정(스네이크로)

}
