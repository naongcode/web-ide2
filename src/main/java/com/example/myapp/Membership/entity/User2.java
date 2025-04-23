package com.example.myapp.Membership.entity;

import com.example.myapp.IDE.entity.Quest;
import com.example.myapp.IDE.entity.Submission;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "user")
public class User2 {

    @Id
    @Column(name = "user_id", length = 10)
    private String userId;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 255) //hashed_password -> password로 수정
    private String hashedPassword;

    @Column(name = "tier", length = 20)
    private String tier;

    @Column(name = "last_tier_updated_at") 
    private Date lastTierUpdatedAt;


    @OneToMany(mappedBy = "leaderId")
    @JsonManagedReference
    private List<Team2> ledTeams;


    @OneToMany(mappedBy = "userId")
    private List<TeamMember2> teamMembers;

    @OneToMany(mappedBy = "creatorId")
    private List<Quest2> createdQuests;

    @OneToMany(mappedBy = "userId")
    private List<Submission2> submissions;

    @OneToMany(mappedBy = "userId")
    private List<Chat2> chats;
}