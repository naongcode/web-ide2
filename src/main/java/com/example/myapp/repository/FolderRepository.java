package com.example.myapp.repository;

import com.example.myapp.IDE.Folder.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByParentId_FolderId(Long parentId);

}