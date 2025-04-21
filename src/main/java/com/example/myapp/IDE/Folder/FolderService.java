package com.example.myapp.IDE.Folder;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.example.myapp.repository.FolderRepository;
@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    // SLF4J Logger 추가
    private static final Logger logger = LoggerFactory.getLogger(FolderService.class);

    public Folder createFolder(FolderCreateRequest request) {
        Folder parentFolder = null;

        logger.info("Received request to create folder: teamId={}, questId={}, userId={}, folderName={}",
                request.getTeamId(), request.getQuestId(), request.getUserId(), request.getFolderName());

        // 부모 폴더가 있는 경우, 해당 폴더를 찾음
        if (request.getParentId() != 0) {
            try {
                parentFolder = folderRepository.findById(request.getParentId())
                        .orElseThrow(() -> new IllegalArgumentException("부모 폴더가 존재하지 않습니다."));
                logger.info("Parent folder found: parentFolderId={}", parentFolder.getFolderId());
            } catch (IllegalArgumentException e) {
                logger.error("Error while finding parent folder: {}", e.getMessage());
                throw e;
            }
        } else {
            logger.info("No parent folder specified, creating root folder.");
        }

        // 새 폴더 생성
        Folder folder = Folder.builder()
                .teamId(request.getTeamId())
                .questId(request.getQuestId())
                .userId(request.getUserId())
                .folderName(request.getFolderName())
                .parentId(parentFolder)
                .build();

        logger.info("Saving new folder: folderName={}, teamId={}, questId={}", folder.getFolderName(), folder.getTeamId(), folder.getQuestId());

        // 폴더 저장
        Folder savedFolder = folderRepository.save(folder);

        logger.info("Folder created successfully: folderId={}", savedFolder.getFolderId());

        return savedFolder;
    }
}
