package com.example.myapp.question;

import com.example.myapp.IDE.entity.TeamMember;
import com.example.myapp.IDE.entity.Submission;
import com.example.myapp.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSubmissionStatusService {

    private final TeamMemberRepository teamMemberRepository;
    private final SubmissionRepository submissionRepository;

    // 팀원 전체에 대한 문제 제출 여부 확인
    public List<UserSubmissionStatusDto> getUserSubmissionStatus(Long teamId, Long questId) {
        // 팀원 목록 조회
        List<TeamMember> teamMembers = teamMemberRepository.findByTeam_TeamId(teamId);

        // 각 팀원에 대해 제출 여부 확인
        return teamMembers.stream().map(member -> {
            // 유저의 userId를 가져오기
            String userId = member.getUser().getUserId(); // getUser() 메서드를 통해 User 객체를 가져오고 userId를 얻음
            String nickname = member.getUser().getNickname();

            // 제출 여부 확인
            Submission submission = submissionRepository.findByQuest_QuestIdAndUser_UserId(questId, userId)
                    .orElse(null); // 제출이 없으면 null을 반환

            // is_completed 필드를 직접 반환
            boolean isCompleted = (submission != null && submission.isCompleted()) ? true : false; // 필드 접근

            // UserSubmissionStatusDto 생성
            return UserSubmissionStatusDto.builder()
                    .userId(member.getUser().getUserId())  // userId
                    .nickname(member.getUser().getNickname())  // nickname 추가
                    .isCompleted(isCompleted)  // isCompleted
                    .build();
        })
        .collect(Collectors.toList());
    }
}