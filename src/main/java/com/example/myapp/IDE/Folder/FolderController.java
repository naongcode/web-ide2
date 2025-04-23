package com.example.myapp.IDE.Folder;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/code/{teamId}/{questId}/{userId}")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    private static final Logger logger = LoggerFactory.getLogger(FolderService.class);

    // 폴더 추가
    @PostMapping("/folder")
    public ResponseEntity<FolderCreateResponse> createFolder(@PathVariable Long teamId,
                                                             @PathVariable Long questId,
                                                             @PathVariable String userId,
                                                             @RequestBody FolderCreateRequest request) {

        logger.info("Received request to create folder: teamId={}, questId={}, userId={}, folderName={}",
                teamId, questId, userId, request.getFolderName());

        // 요청 데이터에서 teamId, questId, userId를 세팅
        request.setTeamId(teamId);
        request.setQuestId(questId);
        request.setUserId(userId);

        // 폴더 생성
        Folder folder = folderService.createFolder(request);

        // 응답 DTO 생성
        FolderCreateResponse response = FolderCreateResponse.builder()
                .folderId(folder.getFolderId())
                .folderName(folder.getFolderName())
                .parentId(folder.getParentId() != null ? folder.getParentId().getFolderId() : 0L)
                .createdAt(folder.getCreatedAt())
//                .teamId(folder.getTeamId()) 팀, 퀘스트, 유저 정보 추가(사용하지 않는 정보 즉, 데이터베이스에는 컬럼이 없음)
//                .questId(folder.getQuestId())
//                .userId(folder.getUserId())
                .build();

        return ResponseEntity.ok(response);
    }
}
