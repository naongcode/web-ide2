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
        log.info("getCodeIndex 호출 (수정됨 - 파일 기준, 파일 있는 폴더만 조회) - questId: {}, userId: {}", questId, userId);

        // 1. 특정 퀘스트에 대한 사용자 제출물에 연결된 모든 파일 조회
        List<File> files = fileRepository.findBySubmission_Quest_QuestIdAndSubmission_User_UserId(questId, userId);
        log.debug("조회된 파일 목록: {}", files);

        // 폴더 ID를 키로 사용하는 맵
        Map<Long, FolderInfo> folderMap = new LinkedHashMap<>();
        // 폴더 없는 파일들을 담을 리스트
        List<FileInfo> rootFilesList = new ArrayList<>();

        // 2. 파일 정보를 기반으로 폴더 구조 생성 및 파일 분류
        for (File file : files) {
            FileInfo fileInfo = new FileInfo(
                    file.getFileId(),
                    file.getFileName(),
                    file.getCodeContext(),
                    file.getLanguage()
            );
            Folder folder = file.getFolder();

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
        List<FolderInfo> result = new ArrayList<>(folderMap.values());

        // 폴더에 속하지 않은 파일들을 위한 FolderInfo 객체 생성 및 추가
        if (!rootFilesList.isEmpty()) {
            Long rootFolderId = -1L;
            String rootFolderName = "Root Files";
            FolderInfo rootFolderInfo = new FolderInfo(
                    rootFolderId,
                    rootFolderName,
                    null,
                    rootFilesList
            );
            log.info("루트 파일 {}개를 포함하는 '{}' 폴더 정보 추가.", rootFilesList.size(), rootFolderName);
            result.add(0, rootFolderInfo); // 또는 원하는 위치에 추가
        }

        log.info("getCodeIndex 로직 완료 (파일 기준, 파일 있는 폴더만 조회), 총 {}개의 폴더/루트 폴더 정보 반환.", result.size());
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