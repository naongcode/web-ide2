package com.example.myapp.IDE.controller;

import com.example.myapp.IDE.dto.FileCreateRequest;
import com.example.myapp.IDE.dto.FileCreateResponse;
import com.example.myapp.IDE.dto.FileUpdateRequest;
import com.example.myapp.IDE.dto.FileUpdateResponse;
import com.example.myapp.IDE.entity.File;
import com.example.myapp.IDE.service.FileService;
import com.example.myapp.Membership.util.extractInfoFromToken;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/code/{teamId}/{questId}")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    // 🔹 1. 파일 추가
    @PostMapping("/file")
    public ResponseEntity<FileCreateResponse> createFile(@PathVariable Long teamId,
                                                         @PathVariable Long questId,
                                                         @RequestBody FileCreateRequest request,
                                                         HttpServletRequest httpRequest) { // HttpServletRequest 파라미터 추가
        logger.info("Received request to create file: teamId={}, questId={}, userId={}, fileName={}",
                teamId, questId, request.getFileName());

        // 수정 -> Authorization 헤더에서 토큰 추출
        String authorizationHeader = httpRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String token = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값
        String userId = extractInfoFromToken.extractUserIdFromToken(token);

        // 경로에서 받은 값 세팅
        request.setTeamId(teamId);
        request.setQuestId(questId);
        request.setUserId(userId); // 어떤 userId를 사용할지 결정

        File file = fileService.createFile(request);

        FileCreateResponse response = FileCreateResponse.builder()
                .fileId(file.getFileId())
                .folderId(file.getFolder() != null ? file.getFolder().getFolderId() : null)
                .fileName(file.getFileName())
                .language(file.getLanguage())
                .teamId(teamId)
                .questId(file.getQuestId())
                .userId(userId) // 어떤 userId를 사용할지 결정
                .createdAt(file.getCreatedAt())
                .build();

        logger.info("📤 Returning Response: folderId={}",
                file.getFolder() != null ? file.getFolder().getFolderId() : "null");

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
