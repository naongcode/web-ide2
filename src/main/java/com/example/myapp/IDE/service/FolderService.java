package com.example.myapp.IDE.service;

import com.example.myapp.IDE.dto.FolderCreateRequest;
import com.example.myapp.IDE.entity.Folder;
import org.springframework.stereotype.Service;
import com.example.myapp.repository.FolderRepository;
@Service
public class FolderService {

    private final FolderRepository folderRepository;

    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public Folder createFolder(FolderCreateRequest request) {
        Folder parentFolder = null;
        Long parentId = request.getParent_id(); //수정(스네이크로)

        // 수정 부분 -> parentId가 null이면 루트 폴더 생성
        if (parentId != null) {
            parentFolder = folderRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 폴더가 존재하지 않습니다."));
        }

        Folder newFolder = Folder.builder()
                //.teamId(request.getTeamId())
                //.questId(request.getQuestId()) 팀, 퀘스트, 유저 정보 추가(사용하지 않는 정보 즉, 데이터베이스에는 컬럼이 없음)
                //.userId(request.getUserId())
                .folderName(request.getFolder_name()) //수정(스네이크로)
                .parentId(parentFolder) // parentId가 null이면 parent는 null
                .build();

        return folderRepository.save(newFolder);
    }
}
