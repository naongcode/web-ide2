package com.example.myapp.IDE.service;

import com.example.myapp.IDE.dto.CodeRunRequest;
import com.example.myapp.IDE.dto.CodeRunResponse;
import com.example.myapp.IDE.entity.Quest;
import com.example.myapp.IDE.entity.Submission;
import com.example.myapp.IDE.entity.User;
import com.example.myapp.IDE.dto.SubmissionRequest;
import com.example.myapp.IDE.dto.SubmissionResponse;
import com.example.myapp.repository.QuestRepository;
import com.example.myapp.repository.SubmissionRepository;
import com.example.myapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final CodeService codeService; // CodeService 주입


    @Override
    public SubmissionResponse submit(SubmissionRequest request) {

        // 유저, 퀘스트가 맞으면 진행을 하겠다.
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Quest quest = questRepository.findById(request.getQuestId())
                .orElseThrow(() -> new IllegalArgumentException("Quest not found"));

        // 코드 실행 요청 DTO 생성
        CodeRunRequest codeRunRequest = new CodeRunRequest();
        codeRunRequest.setCode_context(request.getCodeContext());
        codeRunRequest.setLanguage(request.getLanguage());
        // 어떤 언어로 실행해야 할지 정보가 필요
        // Quest 엔티티나 SubmissionRequest에 언어 정보가 활용할 수 있음.

        // 코드 실행
        CodeRunResponse codeRunResponse = codeService.runCode(codeRunRequest);
        String executionResult = codeRunResponse.getOutput();


        // 제출 엔티티 생성 (DB 저장을 위함)
        Submission submission = Submission.builder()
                .user(user)
                .quest(quest)
                .codeContext(request.getCodeContext())
                .output(executionResult) // 코드 실행 결과 저장
                .submittedAt(new Date())
                .isCompleted(request.getIsCompleted())
                .language(request.getLanguage())
                .build();

        // 저장해라.
        submissionRepository.save(submission);

        // 응답 DTO에 전달
        return SubmissionResponse.builder()
                .submissionId(submission.getSubmissionId())
                .output(submission.getOutput())
                .language(request.getLanguage())
                .submittedAt(submission.getSubmittedAt())
                .build();
    }
}