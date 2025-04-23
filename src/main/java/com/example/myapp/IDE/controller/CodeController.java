package com.example.myapp.IDE.controller;

import com.example.myapp.IDE.dto.CodeStatusResponse;
import com.example.myapp.IDE.dto.CodeStatusUpdateRequest;
import com.example.myapp.IDE.dto.FolderInfo;
import com.example.myapp.IDE.service.CodeService;
import com.example.myapp.IDE.dto.CodeRunRequest;
import com.example.myapp.IDE.dto.CodeRunResponse;
import com.example.myapp.Membership.util.extractInfoFromToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.myapp.Membership.config.AppConfig;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/code")
public class CodeController {

    private final CodeService codeService;

    //코드목록조회 엔드포인트
    @GetMapping("/{questId}/{userId}")
    public ResponseEntity<List<FolderInfo>> getCodeIndex(
            @PathVariable("questId") Long questId,
            @PathVariable("userId") String userId
    ) {
        return ResponseEntity.ok(codeService.getCodeIndex(questId, userId));
    }


    // 코드상태변경 엔드포인트 수정 - > userId를 토큰에서 얻는 방식
    @PatchMapping("/{team_id}/{quest_id}/status") // URL 경로는 정의와 동일하게 유지
    public ResponseEntity<CodeStatusResponse> updateCodeStatus(
            @PathVariable("team_id") Long teamId, // URL에서 team_id 획득
            @PathVariable("quest_id") Long questId, // URL에서 quest_id 획득
            @RequestHeader("Authorization") String authorizationHeader, // <-- 토큰 헤더 다시 추가
            @RequestBody CodeStatusUpdateRequest request // <-- user_id 필드가 제거된 DTO 사용
    )  {
        // 토큰에서 userId 추출
        String token = authorizationHeader.replace("Bearer ", "");
        String userIdFromToken = extractInfoFromToken.extractUserIdFromToken(token); // <-- userId를 토큰에서 얻음

        // 요청 본문 DTO에서 상태 정보 및 기타 필드 추출
        String questStatus = request.getQuest_status();

        // Service 메소드 호출 시 URL에서 얻은 teamId, questId와 토큰에서 얻은 userId 전달
        boolean success = codeService.updateQuestStatus(teamId, questId, userIdFromToken, questStatus);

        if (success) {
            //성공 응답
            return ResponseEntity.ok(new CodeStatusResponse(true));
        } else {
            //실패 응답
            return ResponseEntity.badRequest().body(new CodeStatusResponse(false));
        }
    }

    //코드 실행 엔드포인트
    @PostMapping("/run")
    public ResponseEntity<CodeRunResponse> runCode(@RequestBody CodeRunRequest request) {
        CodeRunResponse response = codeService.runCode(request);
        return ResponseEntity.ok(response);
    }


}