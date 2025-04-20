package com.example.myapp.IDE.File;

import com.example.myapp.IDE.Folder.Folder;
import com.example.myapp.IDE.repository.FolderRepository;
import com.example.myapp.IDE.repository.FileRepository;
import com.example.myapp.entity.User;
import com.example.myapp.repository.SubmissionRepository;
import com.example.myapp.repository.QuestRepository;
import com.example.myapp.entity.Submission;
import com.example.myapp.entity.Quest;
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
        if (request.getFolderId() != null) {
            folder = folderRepository.findById(request.getFolderId())
                    .orElseThrow(() -> new IllegalArgumentException("Folder not found"));
        }

        Submission submission = submissionRepository.findByQuest_questIdAndUser_UserId(request.getQuestId(), request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Submission not found"));

        // Quest와 User 정보는 Submission에서 가져올 수 있음
        Quest quest = submission.getQuest();
        User user = submission.getUser();

        File file = File.builder()
                .folder(folder)
                .fileName(request.getFileName())
                .language(request.getLanguage())
                .teamId(request.getTeamId())
                .questId(request.getQuestId())
                .userId(request.getUserId())
                .createdAt(new Date())
                .build();

        return fileRepository.save(file);
    }

    @Override
    public File updateFile(FileUpdateRequest request) {
        File file = fileRepository.findById(request.getFileId())
                .orElseThrow(() -> new IllegalArgumentException("File not found"));

        if (request.getFolderId() != null) {
            Folder folder = folderRepository.findById(request.getFolderId())
                    .orElseThrow(() -> new IllegalArgumentException("Folder not found"));
            file.setFolder(folder);
        }

        if (request.getFileName() != null) {
            file.setFileName(request.getFileName());
        }

        if (request.getContent() != null) {
            file.setContent(request.getContent());
        }

        file.setUpdatedAt(new Date());

        return fileRepository.save(file);
    }
}
