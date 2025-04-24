package com.example.myapp.question;

import com.example.myapp.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SubmissionRepository submissionRepository;

    // 문제 생성
    public QuestionCreateResponseDto createQuestion(QuestionRequestDto requestDto) {
        Question question = Question.builder()
                .teamId(requestDto.getTeamId())
                .creatorId(requestDto.getCreatorId())
                .questName(requestDto.getQuestName())
                .questStart(requestDto.getQuestStart())
                .questDue(requestDto.getQuestDue())
                .questLink(requestDto.getQuestLink())
                .build();

        questionRepository.save(question);

        return QuestionCreateResponseDto.builder()
                .success(true)
                .message("문제가 성공적으로 생성되었습니다.")
                .build();
    }

    // 문제 상세 조회 (팀 ID + 문제 ID)
    public QuestionResponseDto getQuestionDetail(Long teamId, Long questId) {
        Question question = questionRepository.findById(questId)
                .filter(q -> q.getTeamId().equals(teamId))
                .orElseThrow(() -> new IllegalArgumentException("해당 팀의 문제가 존재하지 않습니다."));

        // questStatus 계산 (현재 날짜 기준)
        LocalDate today = LocalDate.now();
        String questStatus = today.isAfter(question.getQuestDue()) ? "마감" : "진행중";

        // 유저가 해당 문제를 제출했는지 확인
//        boolean isSubmitted = submissionRepository.existsByQuest_QuestIdAndUser_UserId(questId, userId);

        return QuestionResponseDto.builder()
                .questId(question.getQuestId())
                .teamId(question.getTeamId())
                .creatorId(question.getCreatorId())
                .questName(question.getQuestName())
                .questStart(question.getQuestStart())
                .questDue(question.getQuestDue())
                .questLink(question.getQuestLink())
                .questStatus(questStatus)
//                .isSubmitted(isSubmitted) // 유저가 제출했는지 정보 추가
                .build();
    }

    // 문제 상세 조회 (팀 ID + 문제 ID + 유저 ID)
    public QuestionResponseDto getQuestionDetailWithUserId(Long teamId, Long questId, String userId) {
        Question question = questionRepository.findById(questId)
                .filter(q -> q.getTeamId().equals(teamId))
                .orElseThrow(() -> new IllegalArgumentException("해당 팀의 문제가 존재하지 않습니다."));

        LocalDate today = LocalDate.now();
        String questStatus = today.isAfter(question.getQuestDue()) ? "마감" : "진행중";

        boolean isSubmitted = submissionRepository.existsByQuest_QuestIdAndUser_UserId(questId, userId);

        return QuestionResponseDto.builder()
                .questId(question.getQuestId())
                .teamId(question.getTeamId())
                .creatorId(question.getCreatorId())
                .questName(question.getQuestName())
                .questStart(question.getQuestStart())
                .questDue(question.getQuestDue())
                .questLink(question.getQuestLink())
                .questStatus(questStatus)
                .isSubmitted(isSubmitted)
                .build();
    }

    // 팀별 문제 목록 조회 (유저별 제출 여부 확인 제거)
    public List<QuestionResponseDto> getQuestionsByTeam(Long teamId) {
        return questionRepository.findByTeamId(teamId).stream()
                .map(q -> {
                    String questStatus = LocalDate.now().isAfter(q.getQuestDue()) ? "마감" : "진행중";
                    return QuestionResponseDto.builder()
                            .questId(q.getQuestId())
                            .teamId(q.getTeamId())
                            .creatorId(q.getCreatorId())
                            .questName(q.getQuestName())
                            .questStart(q.getQuestStart())
                            .questDue(q.getQuestDue())
                            .questLink(q.getQuestLink())
                            .questStatus(questStatus)
                            .build();
                })
                .toList();
    }
}
