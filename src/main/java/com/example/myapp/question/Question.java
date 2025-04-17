package com.example.myapp.question;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor  // 기본 생성자
@AllArgsConstructor  // 모든 필드를 사용하는 생성자
@Table(name="quest")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id")
    private Long questId;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "creator_id")
    private String creatorId;

    @Column(name = "quest_name")
    private String questName;

    @Column(name = "quest_start")
    private LocalDate questStart;

    @Column(name = "quest_due")
    private LocalDate questDue;

    @Column(name = "quest_link")
    private String questLink;

    @Column(name = "quest_status")
    @Enumerated(EnumType.STRING)
    private QuestStatus questStatus;
}
