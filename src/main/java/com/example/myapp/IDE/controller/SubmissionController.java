package com.example.myapp.IDE.controller;

import com.example.myapp.IDE.service.SubmissionService;
import com.example.myapp.IDE.dto.SubmissionRequest;
import com.example.myapp.IDE.dto.SubmissionResponse;
import com.example.myapp.Membership.util.ExtractInfoFromToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/submission")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    // 코드 제출 요청
    @PostMapping("/")
    public ResponseEntity<SubmissionResponse> submitCode(
            @RequestHeader("Authorization") String authorizationHeader, // 토큰헤더
            @RequestBody SubmissionRequest request // questId와 is_completed 받기
    ) {
        log.info("Received request to submit code: {}", request);

        try {
            // 토큰에서 userId 추출
            String token = authorizationHeader.replace("Bearer ", "");
            String userId = ExtractInfoFromToken.extractUserIdFromToken(token);

            // 요청 객체에 userId 세팅
            request.setUserId(userId);

            // 코드 제출 로직 처리
            SubmissionResponse response = submissionService.submit(request);
            log.info("Code submitted successfully: {}", response); // 성공적인 제출 로그
            return ResponseEntity.ok(response); // 응답 리턴
        } catch (Exception e) {
            log.error("Error while submitting code: {}", e.getMessage(), e); // 에러 발생 시 로그
            return ResponseEntity.status(500).body(null); // 서버 에러 응답
        }
    }
}
