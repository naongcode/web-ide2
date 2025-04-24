package com.example.myapp.question;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quest/status/{teamId}/{questId}")
@RequiredArgsConstructor
public class UserSubmissionStatusController {

    private final UserSubmissionStatusService userSubmissionStatusService;

    // 유저 제출 상태 조회
    @GetMapping
    public ResponseEntity<List<UserSubmissionStatusDto>> getUserSubmissionStatus(
            @PathVariable Long teamId,
            @PathVariable Long questId) {

        List<UserSubmissionStatusDto> response = userSubmissionStatusService.getUserSubmissionStatus(teamId, questId);

        return ResponseEntity.ok(response);
    }
}
