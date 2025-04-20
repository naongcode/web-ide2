package com.example.myapp.Membership.entity;

import com.example.myapp.Membership.entity.Chat;
import com.example.myapp.IDE.entity.Quest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "team_name", nullable = false, length = 50, unique = true)
    private String teamName;

    @Column(name = "team_description", columnDefinition = "TEXT")
    private String teamDescription;

    @Column(name = "max_member", nullable = false)
    private Integer maxMember;

    @Column(name = "current_member_count", columnDefinition = "INT DEFAULT 1")
    private Integer currentMemberCount;

    @ManyToOne
    @JoinColumn(name = "leader_id", nullable = false)
    private User leaderId;

    @Column(name = "team_tier", length = 20)
    private String teamTier;

    @OneToMany(mappedBy = "teamId")
    private List<TeamMember> teamMembers;
    public Team(String teamName, String teamDescription, int maxmember, String teamTier, User leader) {
        this.teamName = teamName;
        this.teamDescription = teamDescription;
        this.maxMember = maxmember;
        this.teamTier = teamTier;
        this.leaderId = leader;
        this.currentMemberCount = 1;
        this.teamMembers = new ArrayList<>();
    }

}