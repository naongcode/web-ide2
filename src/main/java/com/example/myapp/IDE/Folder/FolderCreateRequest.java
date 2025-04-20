package com.example.myapp.IDE.Folder;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderCreateRequest {

    private Long teamId;

    private Long questId;

    private String userId;

    private Long parentId; // null이면 최상위 폴더

    private String name; // 폴더 이름

    private String folderName; // 폴더 이름

}
