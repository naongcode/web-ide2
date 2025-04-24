package com.example.myapp.question;

import com.example.myapp.Membership.util.extractInfoFromToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quest/access")
public class QuestionAccessController {

    private final QuestionAccessService questionAccessService;

    /**
     * 코드 편집기 접근을 위한 유효성 검사용 엔드포인트
     */
    @GetMapping("/{teamId}/{questId}/{targetUserId}")
    public ResponseEntity<QuestionAccessResponse> validateEditorAccess(
            @PathVariable Long teamId,
            @PathVariable Long questId,
            @PathVariable String targetUserId,
            HttpServletRequest request
    ) {
        // 1. 토큰에서 유저 ID 추출
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String tokenUserId = extractInfoFromToken.extractUserIdFromToken(token);

        // 2. 서비스 호출
        QuestionAccessResponse response =
                questionAccessService.validateEditorAccess(teamId, tokenUserId, questId, targetUserId);

        return ResponseEntity.ok(response);
    }
}
