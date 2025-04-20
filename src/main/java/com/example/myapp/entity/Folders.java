package com.example.myapp.entity;

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
public class Folders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private Long folderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id", nullable = false)
    private Quest quest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "folder_name", nullable = false)
    private String folderName;

    // 부모 폴더와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "folder_id")
    private Folders parentFolder;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Folders> childFolders = new ArrayList<>();

    // 폴더 안의 파일들과의 관계 설정
    @OneToMany(mappedBy = "folder")
    private List<Files> files;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    // 추가된 기본 생성자
    public Folders() {
        // 기본 생성자
    }

}
