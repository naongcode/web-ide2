package com.example.myapp.IDE.Folder;

import com.example.myapp.IDE.File.File;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "folder")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private Long folderId;

    // 팀, 퀘스트, 유저 정보 추가
    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "quest_id", nullable = false)
    private Long questId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "folder_name", nullable = false)
    private String folderName;

    // 부모 폴더와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "folder_id")
    private Folder parentId;

    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Folder> childFolders = new ArrayList<>();

    // 폴더 안의 파일들과의 관계 설정
    @OneToMany(mappedBy = "folder")
    private List<File> files;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    // 추가된 기본 생성자
    public Folder() {
        // 기본 생성자
    }

}
