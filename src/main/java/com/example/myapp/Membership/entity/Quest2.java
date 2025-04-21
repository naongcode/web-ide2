package com.example.myapp.Membership.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quest")
public class Quest2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id")
    private Integer questId;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team2 teamId;

    @Column(name = "quest_name", nullable = false, length = 100)
    private String questName;

    @Column(name = "quest_start", nullable = false)
    private Date questStart;

    @Column(name = "quest_due", nullable = false)
    private Date questDue;

    @Column(name = "quest_link", length = 255)
    private String questLink;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id")
    private User2 creatorId;

    @Column(name = "quest_status", nullable = false, length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'IN_PROGRESS'")
    private String quesStatus;

    @OneToMany(mappedBy = "questId")
    private List<Submission2> submissions;

    @OneToMany(mappedBy = "questId")
    private List<File2> files;
}