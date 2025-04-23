package com.example.myapp.question;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quest/access")
public class QuestionAccessController {

    private final QuestionAccessService questionAccessService;

    /**
     * 코드 편집기 접근을 위한 유효성 검사용 엔드포인트
     */
    @GetMapping("/{teamId}/{questId}/{userId}")
    public ResponseEntity<QuestionAccessResponse> validateEditorAccess(
            @PathVariable Long teamId,
            @PathVariable Long questId,
            @PathVariable String userId) {

        QuestionAccessResponse response = questionAccessService.validateEditorAccess(teamId, userId, questId);
        return ResponseEntity.ok(response);
    }
}
