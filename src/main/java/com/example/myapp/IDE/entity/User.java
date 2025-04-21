package com.example.myapp.IDE.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "user_id", length = 10)
    private String userId;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "hashed_password", nullable = false, length = 255)
    private String hashedPassword;

    @Column(name = "tier", length = 20)
    private String tier;

    @Column(name = "last_tier_updated_at") // 이 부분이 중요
    private Date lastTierUpdatedAt;


//    @OneToMany(mappedBy = "leaderId")
//    private List<Team> ledTeam;
//
//
//    @OneToMany(mappedBy = "userId")
//    private List<TeamMember> teamMember;
//
//    @OneToMany(mappedBy = "creatorId")
//    private List<Quest> createdQuest;
//
//    @OneToMany(mappedBy = "userId")
//    private List<Submission> submission;

}