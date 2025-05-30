package com.example.myapp.Membership.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "folder")
public class Folder2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private Long folderId;

    @Column(name = "folder_name", nullable = false, length = 100)
    private String folderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "folder_id")
    private Folder2 parentId;

    @OneToMany(mappedBy = "folder") // File 엔티티의 folder 필드와 매핑
    private List<File2> files;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
}