package com.example.myapp.IDE.service;

import com.example.myapp.IDE.dto.*;
import com.example.myapp.IDE.entity.File;
import com.example.myapp.IDE.entity.Folder;
import com.example.myapp.IDE.entity.Quest;
import com.example.myapp.repository.FolderRepository;
import com.example.myapp.repository.FileRepository;


import com.example.myapp.repository.QuestRepository;
import com.example.myapp.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CodeService {

    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;
    private final SubmissionRepository submissionRepository;
    private final QuestRepository questRepository; // 필요하다면 사용

    //코드 실행 API들의 주소와 ID (필요하다면 유지)
    @Value("${jdoodle.api.url}")
    private String jdoodleApiUrl;
    @Value("${jdoodle.client.id}")
    private String jdoodleClientId;
    @Value("${jdoodle.client.secret}")
    private String jdoodleClientSecret;

    private final RestTemplate restTemplate; // 필요하다면 유지

    // 코드 목록 조회 로직 (수정됨: 폴더에 속하지 않은 파일 포함)
    public List<FolderInfo> getCodeIndex(Long questId, String userId) {
        log.info("getCodeIndex 호출 - questId: {}, userId: {}", questId, userId);

        // 1. 특정 퀘스트에 대한 사용자 제출물에 연결된 모든 파일 조회
        // FileRepository에 findBySubmission_Quest_QuestIdAndSubmission_User_UserId 메소드가 선언되어 있어야 합니다.
        List<File> files = fileRepository.findBySubmission_Quest_QuestIdAndSubmission_User_UserId(questId, userId);

        // 파일이 없는 경우 빈 목록 반환
        if (files.isEmpty()) {
            log.info("해당 퀘스트/사용자에 대한 파일이 없습니다.");
            return new ArrayList<>();
        }

        // 2. 파일들을 폴더별로 그룹화하고, 폴더 없는 파일 분리
        // 폴더 ID를 키로 사용하는 맵 (폴더 있는 파일용)
        Map<Long, FolderInfo> folderMap = new LinkedHashMap<>();
        // 폴더 없는 파일들을 담을 리스트
        List<FileInfo> rootFilesList = new ArrayList<>();

        for (File file : files) {
            Folder folder = file.getFolder(); // 파일이 속한 폴더 엔티티 가져오기

            FileInfo fileInfo = new FileInfo(
                    file.getFileId(),
                    file.getFileName(),
                    file.getCodeContext(),
                    file.getLanguage()
            );

            if (folder == null) {
                // 파일이 폴더에 속하지 않은 경우
                log.debug("파일 {} (ID: {})은/는 폴더에 속해있지 않습니다. 루트 파일로 처리.", file.getFileName(), file.getFileId());
                rootFilesList.add(fileInfo);
            } else {
                // 파일이 폴더에 속한 경우
                // 해당 폴더가 아직 맵에 없으면 추가
                if (!folderMap.containsKey(folder.getFolderId())) {
                    Long parentFolderId = (folder.getParentId() != null) ? folder.getParentId().getFolderId() : null;
                    folderMap.put(folder.getFolderId(), new FolderInfo(
                            folder.getFolderId(),
                            folder.getFolderName(),
                            parentFolderId,
                            new ArrayList<>() // 파일 목록 초기화
                    ));
                    log.debug("폴더 {} (ID: {}) 맵에 추가됨.", folder.getFolderName(), folder.getFolderId());
                }

                // 현재 파일 정보를 해당 폴더의 파일 목록에 추가
                folderMap.get(folder.getFolderId()).getFiles().add(fileInfo);
                log.debug("파일 {} (ID: {}) 폴더 {}에 추가됨.", file.getFileName(), file.getFileId(), folder.getFolderName());
            }
        }

        // 3. 최종 결과 리스트 구성
        List<FolderInfo> result = new ArrayList<>();

        // 폴더에 속한 파일들로 구성된 FolderInfo 객체들을 결과 리스트에 추가
        result.addAll(folderMap.values());

        // 폴더에 속하지 않은 파일들이 있다면, 이들을 위한 특별한 FolderInfo 객체를 만들어 결과 리스트에 추가
        if (!rootFilesList.isEmpty()) {
            // 폴더 없는 파일들을 위한 가상 폴더 정보 생성
            // ID는 실제 폴더와 겹치지 않도록 음수 값 등을 사용할 수 있습니다.
            Long rootFolderId = -1L; // 예: -1
            String rootFolderName = "Root Files"; // 또는 "Unassigned Files" 등 원하는 이름

            FolderInfo rootFolderInfo = new FolderInfo(
                    rootFolderId,
                    rootFolderName,
                    null, // 루트 폴더는 부모가 없습니다.
                    rootFilesList // 폴더 없는 파일 목록을 여기에 추가
            );
            log.info("루트 파일 {}개를 포함하는 '{}' 폴더 정보 추가.", rootFilesList.size(), rootFolderName);

            // 결과 리스트의 어디에 추가할지 결정 (예: 항상 맨 앞에)
            result.add(0, rootFolderInfo); // 맨 앞에 추가
            // 또는 맨 뒤에 추가: result.add(rootFolderInfo);
        }

        log.info("getCodeIndex 로직 완료, 총 {}개의 폴더/루트 폴더 정보 반환.", result.size());
        return result;
    }

    //코드 업데이트 로직
    public boolean updateQuestStatus(Long teamId, Long questId, String userId, String questStatus) {
        log.info("updateQuestStatus 호출 - teamId: {}, questId: {}, userId: {}, questStatus: {}", teamId, questId, userId, questStatus);
        // 실제 퀘스트 상태 업데이트 로직 (기존 코드와 거의 동일)
        Optional<Quest> questOptional = questRepository.findById(questId); // <-- 검증을 위에서 하지 않았다면 여기서 다시 조회

        if (questOptional.isPresent()) {
            Quest quest = questOptional.get();
            log.info("퀘스트 조회 성공 - quest: {}", quest);

            // 퀘스트 상태 업데이트
            quest.setQuesStatus(questStatus);
            log.info("퀘스트 상태 업데이트 시도 - newStatus: {}", quest.getQuesStatus());

            // 데이터베이스에 저장
            questRepository.save(quest);
            log.info("퀘스트 상태 업데이트 성공");
            return true; // 성공
        } else {
            log.warn("해당 questId로 퀘스트를 찾을 수 없음 - questId: {}", questId);
            return false; // 퀘스트를 찾지 못한 경우 실패
        }
    }


    //코드 실행 로직
    public CodeRunResponse runCode(CodeRunRequest clientRequest) {
        JdoodleRequest jdoodleRequest = new JdoodleRequest();
        jdoodleRequest.setScript(clientRequest.getCode_context());
        jdoodleRequest.setLanguage(clientRequest.getLanguage());
        jdoodleRequest.setClientId(jdoodleClientId);
        jdoodleRequest.setClientSecret(jdoodleClientSecret);

        JdoodleResponse jdoodleResponse = restTemplate.postForObject(
                jdoodleApiUrl,
                jdoodleRequest,
                JdoodleResponse.class
        );

        if (jdoodleResponse != null) {
            log.info("JDOodle API 응답: {}", jdoodleResponse);
            return new CodeRunResponse(jdoodleResponse.getOutput());
        } else {
            log.error("JDOodle API 응답 오류");
            return new CodeRunResponse("코드 실행 오류");
        }
    }
}