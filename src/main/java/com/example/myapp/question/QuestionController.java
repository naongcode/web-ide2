package com.example.myapp.question;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/quest")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    // 문제 생성
    @PostMapping
    public ResponseEntity<QuestionCreateResponseDto> createQuestion(@RequestBody QuestionRequestDto requestDto) {
        QuestionCreateResponseDto response = questionService.createQuestion(requestDto);

        System.out.println("받은 questDue: " + requestDto); // ⭐️ 로그 찍기
        log.info("받은 요청: {}", requestDto);

        return ResponseEntity.ok(response);
    }

    // 문제 상세 조회 (팀 ID + 문제 ID )  userID는 제거함
    @GetMapping("/{teamId}/{questId}")
    public ResponseEntity<QuestionResponseDto> getQuestionDetail(
            @PathVariable Long teamId,
            @PathVariable Long questId
//            @PathVariable String userId
    ) {
        QuestionResponseDto response = questionService.getQuestionDetail(teamId, questId);
        return ResponseEntity.ok(response);
    }

    // 문제 상세 조회 (팀 ID + 문제 ID + 유저 ID)
    @GetMapping("/{teamId}/{questId}/{userId}")
    public ResponseEntity<QuestionResponseDto> getQuestionDetailWithUserId(
            @PathVariable Long teamId,
            @PathVariable Long questId,
            @PathVariable String userId
    ) {
        QuestionResponseDto response = questionService.getQuestionDetailWithUserId(teamId, questId, userId);
        return ResponseEntity.ok(response);
    }

    // 팀별 문제 목록 조회
    @GetMapping("/{teamId}")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionsByTeam(@PathVariable Long teamId) {
        List<QuestionResponseDto> response = questionService.getQuestionsByTeam(teamId);
        return ResponseEntity.ok(response);
    }
}
