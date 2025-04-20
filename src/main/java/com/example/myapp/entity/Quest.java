package com.example.myapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "quest")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id")
    private Long questId;

    // 팀 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "quest_name", nullable = false)
    private String questName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "quest_start", nullable = false)
    private Date questStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "quest_due", nullable = false)
    private Date questDue;

    @Column(name = "quest_link")
    private String questLink;

    // 유저 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "quest_status")
    private String questStatus;
}