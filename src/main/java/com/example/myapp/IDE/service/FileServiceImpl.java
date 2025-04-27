package com.example.myapp.IDE.service;

import com.example.myapp.IDE.dto.FileCreateRequest;
import com.example.myapp.IDE.dto.FileUpdateRequest;
import com.example.myapp.IDE.dto.FileUpdateResponse;
import com.example.myapp.IDE.entity.Folder;
import com.example.myapp.IDE.entity.File;
import com.example.myapp.IDE.entity.Quest;
import com.example.myapp.IDE.entity.Submission;
import com.example.myapp.repository.FolderRepository;
import com.example.myapp.repository.FileRepository;
import com.example.myapp.IDE.entity.User;
import com.example.myapp.repository.SubmissionRepository;
import com.example.myapp.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    private final SubmissionRepository submissionRepository;
    private final QuestRepository questRepository;

    @Override
    public File createFile(FileCreateRequest request) {
        Folder folder = null;
        if (request.getFolder_id() != null) {
            folder = folderRepository.findById(request.getFolder_id()) //수정(스네이크로)
                    .orElseThrow(() -> new IllegalArgumentException("Folder not found"));
        }

        Submission submission = submissionRepository.findByQuest_questIdAndUser_UserId(request.getQuest_id(), request.getUser_id()) //수정(스네이크로)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));

        // Quest와 User 정보는 Submission에서 가져올 수 있음
        Quest quest = submission.getQuest();
        User user = submission.getUser();

        File file = File.builder()
                .folder(folder)
                .fileName(request.getFile_name()) //수정(스네이크로)
                .language(request.getLanguage())
                //.teamId(request.getTeamId()) -> 주석처리
                .questId(request.getQuest_id()) //수정(스네이크로)
                //.userId(request.getUserId()) -> 주석처리
                .submission(submission) //추가 -> submission_id 인식을 위함
                .createdAt(new Date())
                .build();

        System.out.println("📦 File will be saved with folder: " + (folder != null ? folder.getFolderId() : "null"));

        return fileRepository.save(file);
    }

    @Override
    public FileUpdateResponse updateFile(FileUpdateRequest request) {
        File file = fileRepository.findById(request.getFile_id())
                .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다."));

        if (request.getFolder_id() != null) {
            Folder folder = folderRepository.findById(request.getFolder_id())
                    .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));
            file.setFolder(folder);
        }

        if (request.getFile_name() != null) {
            file.setFileName(request.getFile_name());
        }

        if (request.getCode_context() != null) {
            file.setCodeContext(request.getCode_context());
        }

        if (request.getLanguage() != null) {
            file.setLanguage(request.getLanguage());
        }

        file.setUpdatedAt(new Date());
        File updatedFile = fileRepository.save(file);

        return FileUpdateResponse.builder()
                .language(updatedFile.getLanguage())
                .updatedAt(updatedFile.getUpdatedAt())
                .build();
    }
}
