package com.example.myapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "submission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submit_id")
    private Long submissionId;

    // 퀘스트 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quest_id", nullable = false) // 외래 키: quest_id
    private Quest quest;

    // 유저 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // User 엔티티 참조

    @Column(name = "code_context", columnDefinition = "TEXT")
    private String codeContext;

    @Column(name = "output", columnDefinition = "TEXT")
    private String output;

    @Column(name = "submitted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt;

    @Column(name = "is_completed")
    private Boolean isCompleted;
}
