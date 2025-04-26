package com.example.myapp.IDE.controller;

import com.example.myapp.IDE.dto.FileCreateRequest;
import com.example.myapp.IDE.dto.FileCreateResponse;
import com.example.myapp.IDE.dto.FileUpdateRequest;
import com.example.myapp.IDE.dto.FileUpdateResponse;
import com.example.myapp.IDE.entity.File;
import com.example.myapp.IDE.service.FileService;
import com.example.myapp.Membership.util.ExtractInfoFromToken;
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
                teamId, questId, request.getFile_name()); //수정(스네이크로)

        // 수정 -> Authorization 헤더에서 토큰 추출
        String authorizationHeader = httpRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String token = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값
        String userId = ExtractInfoFromToken.extractUserIdFromToken(token);

        // 경로에서 받은 값 세팅
        request.setTeam_id(teamId); //수정(스네이크로)
        request.setQuest_id(questId); //수정(스네이크로)
        request.setUser_id(userId); // 어떤 userId를 사용할지 결정 //수정(스네이크로)

        File file = fileService.createFile(request);

        FileCreateResponse response = FileCreateResponse.builder()
                .file_id(file.getFileId()) //수정(스네이크로)
                .folder_id(file.getFolder() != null ? file.getFolder().getFolderId() : null) //수정(스네이크로)
                .file_name(file.getFileName()) //수정(스네이크로)
                .language(file.getLanguage())
                .team_id(teamId) //수정(스네이크로)
                .quest_id(file.getQuestId()) //수정(스네이크로)
                .user_id(userId) // 어떤 userId를 사용할지 결정 //수정(스네이크로)
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
                request.getFile_id(), request.getFile_name()); //수정(스네이크로)

        File updatedFile = fileService.updateFile(request);

        FileUpdateResponse response = FileUpdateResponse.builder()
                .updatedAt(updatedFile.getUpdatedAt())
                .build();

        return ResponseEntity.ok(response);
    }
}
