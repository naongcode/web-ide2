package com.example.myapp.IDE.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor //추가함
@AllArgsConstructor
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "code_context")
    private String codeContext; //수정(code_content -> code_context)

    @Column(name = "language", nullable = false)
    private String language;

    // teamId, questId, userId 추가
//    @Column(name = "team_id", nullable = false) //데이터베이스 컬럼에는 필요없는 정보
//    private Long teamId;

    @Column(name = "quest_id", nullable = false)
    private Long questId;

//    @Column(name = "user_id", nullable = false)  //데이터베이스 컬럼에는 필요없는 정보
//    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false) //수정(submit_id -> submission_id)
    private Submission submission;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;



}
