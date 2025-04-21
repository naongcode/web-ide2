package com.example.myapp.repository;

import com.example.myapp.IDE.File.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByFolder_FolderIdAndSubmission_User_UserIdAndSubmission_Quest_QuestId(
            Long folderId, String userId, Long questId);
}