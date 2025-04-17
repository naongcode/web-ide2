package com.example.myapp.question;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quest")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    // 문제 생성
    @PostMapping
    public ResponseEntity<QuestionCreateResponseDto> createQuestion(@RequestBody QuestionRequestDto requestDto) {
        QuestionCreateResponseDto response = questionService.createQuestion(requestDto);
        return ResponseEntity.ok(response);
    }

    // 문제 상세 조회 (팀 ID + 문제 ID)
    @GetMapping("/{teamId}/{questId}")
    public ResponseEntity<QuestionResponseDto> getQuestionDetail(@PathVariable Long teamId, @PathVariable Long questId) {
        QuestionResponseDto response = questionService.getQuestionDetail(teamId, questId);
        return ResponseEntity.ok(response);
    }

    // 팀별 문제 목록 조회
    @GetMapping("/{teamId}")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionsByTeam(@PathVariable Long teamId) {
        List<QuestionResponseDto> response = questionService.getQuestionsByTeam(teamId);
        return ResponseEntity.ok(response);
    }
}
