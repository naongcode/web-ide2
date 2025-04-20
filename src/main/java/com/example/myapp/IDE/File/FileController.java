package com.example.myapp.IDE.File;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/code/{teamId}/{questId}/{userId}")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    // 🔹 1. 파일 추가
    @PostMapping("/file")
    public ResponseEntity<FileCreateResponse> createFile(@PathVariable Long teamId,
                                                         @PathVariable Long questId,
                                                         @PathVariable String userId,
                                                         @RequestBody FileCreateRequest request) {
        logger.info("Received request to create file: teamId={}, questId={}, userId={}, fileName={}",
                teamId, questId, userId, request.getFileName());

        // 경로에서 받은 값 세팅
        request.setTeamId(teamId);
        request.setQuestId(questId);
        request.setUserId(userId);

        File file = fileService.createFile(request);

        FileCreateResponse response = FileCreateResponse.builder()
                .fileId(file.getFileId())
                .folderId(file.getFolder() != null ? file.getFolder().getFolderId() : null)
                .fileName(file.getFileName())
                .language(file.getLanguage())
                .teamId(file.getTeamId())
                .questId(file.getQuestId())
                .userId(file.getUserId())
                .createdAt(file.getCreatedAt())
                .build();

        return ResponseEntity.ok(response);
    }

    // 🔹 2. 파일 수정
    @PutMapping
    public ResponseEntity<FileUpdateResponse> updateFile(@RequestBody FileUpdateRequest request) {
        logger.info("Received request to update file: fileId={}, fileName={}",
                request.getFileId(), request.getFileName());

        File updatedFile = fileService.updateFile(request);

        FileUpdateResponse response = FileUpdateResponse.builder()
                .updatedAt(updatedFile.getUpdatedAt())
                .build();

        return ResponseEntity.ok(response);
    }
}
