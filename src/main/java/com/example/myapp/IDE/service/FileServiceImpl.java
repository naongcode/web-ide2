package com.example.myapp.IDE.service;

import com.example.myapp.IDE.dto.FileCreateRequest;
import com.example.myapp.IDE.dto.FileUpdateRequest;
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
    public File updateFile(FileUpdateRequest request) {
        File file = fileRepository.findById(request.getFile_id()) //수정(스네이크로)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));

        if (request.getFolder_id() != null) { //수정(스네이크로)
            Folder folder = folderRepository.findById(request.getFolder_id()) //수정(스네이크로)
                    .orElseThrow(() -> new IllegalArgumentException("Folder not found"));
            file.setFolder(folder);
        }

        if (request.getFile_name() != null) { //수정(스네이크로)
            file.setFileName(request.getFile_name()); //수정(스네이크로)
        }

        if (request.getCode_context() != null) { //수정(스네이크로)
            file.setCodeContext(request.getCode_context()); //수정(스네이크로)
        }

        file.setUpdatedAt(new Date());

        return fileRepository.save(file);
    }
}
