package com.example.myapp.question;

import com.example.myapp.IDE.entity.TeamMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionAccessService {

    private final QuestionRepository questionRepository;
    private final TeamMemberRepository teamMemberRepository;

    /**
     * 편집기 접근 가능한 문제인지 확인
     */
    public QuestionAccessResponse validateEditorAccess(
            Long teamId,
            String tokenUserId,
            Long questId,
            String targetUserId

            ) {
        Question question = questionRepository.findById(questId)
                .orElseThrow(() -> new IllegalArgumentException("해당 문제를 찾을 수 없습니다."));

        if (!question.getTeamId().equals(teamId)) {
            throw new IllegalArgumentException("문제가 해당 팀에 속하지 않습니다.");
        }

        boolean isTeamMember = teamMemberRepository.existsByTeam_TeamIdAndUser_UserId(teamId, tokenUserId);
        if (!isTeamMember) {
            throw new IllegalArgumentException("사용자가 팀에 속해 있지 않습니다.");
        }

        // 자기 문제인지 확인 (토큰의 유저가 클릭한 userId와 같은지)
        if (!tokenUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("자신의 문제에만 접근할 수 있습니다.");
        }


        return new QuestionAccessResponse(
                question.getQuestId(),
                question.getQuestName(),
                question.getQuestLink(),
                true
        );
    }
}
