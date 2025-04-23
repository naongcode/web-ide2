package com.example.myapp.IDE.controller;

import com.example.myapp.IDE.dto.FolderCreateRequest;
import com.example.myapp.IDE.dto.FolderCreateResponse;
import com.example.myapp.IDE.service.FolderService;
import com.example.myapp.IDE.entity.Folder;
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
public class FolderController {

    private final FolderService folderService;

    private static final Logger logger = LoggerFactory.getLogger(FolderService.class);

    // 폴더 추가
    @PostMapping("/folder")
    public ResponseEntity<FolderCreateResponse> createFolder(@PathVariable Long teamId,
                                                             @PathVariable Long questId,
                                                             HttpServletRequest httpRequest,
                                                             @RequestBody FolderCreateRequest request) {

        logger.info("Received request to create folder: teamId={}, questId={}, userId={}, folderName={}",
                teamId, questId, request.getFolder_name()); //수정(스네이크로)

        // 수정 -> Authorization 헤더에서 토큰 추출
        String authorizationHeader = httpRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String token = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값
        String userId = extractInfoFromToken.extractUserIdFromToken(token);


        // 요청 데이터에서 teamId, questId, userId를 세팅
        request.setTeam_id(teamId); //수정(스네이크로)
        request.setQuest_id(questId); //수정(스네이크로)
        request.setUser_id(userId); //수정(스네이크로)

        // 폴더 생성
        Folder folder = folderService.createFolder(request);

        // 응답 DTO 생성
        FolderCreateResponse response = FolderCreateResponse.builder()
                .folder_id(folder.getFolderId()) //수정(스네이크로)
                .folder_name(folder.getFolderName()) //수정(스네이크로)
                .parent_id(folder.getParentId() != null ? folder.getParentId().getFolderId() : 0L) //여기서 카멜케이스인건 entity임.
                .createdAt(folder.getCreatedAt())
//                .teamId(folder.getTeamId()) 팀, 퀘스트, 유저 정보 추가(사용하지 않는 정보 즉, 데이터베이스에는 컬럼이 없음)
//                .questId(folder.getQuestId())
//                .userId(folder.getUserId())
                .build();

        return ResponseEntity.ok(response);
    }
}
