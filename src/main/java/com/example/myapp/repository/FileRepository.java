package com.example.myapp.repository;

import com.example.myapp.IDE.File.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findBySubmission_Quest_QuestIdAndSubmission_User_UserId(Long questId, String userId);

    // 오류가 발생한 findBy... 메소드 선언 추가
    List<File> findByFolder_FolderIdAndSubmission_User_UserIdAndSubmission_Quest_QuestId(
            Long folderId, String userId, Long questId
    );


}