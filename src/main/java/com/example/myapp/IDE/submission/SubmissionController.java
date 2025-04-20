package com.example.myapp.IDE.submission;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submission")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    // 코드 제출 요청
    @PostMapping("/{questId}/{userId}")  // questId와 userId를 경로로 받기
    public ResponseEntity<SubmissionResponse> submitCode(
            @PathVariable Long questId,  // URL에서 questId를 추출
            @PathVariable String userId, // URL에서 userId를 추출
            @RequestBody SubmissionRequest request // 요청 본문에서 코드와 제출 완료 여부 받기
    ) {
        // 코드 제출 로직 처리
        SubmissionResponse response = submissionService.submit(request);
        return ResponseEntity.ok(response); // 응답 리턴
    }
}
