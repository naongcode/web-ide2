package com.example.myapp.Membership.repository;

import com.example.myapp.Membership.entity.Team2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository2 extends JpaRepository<Team2, Integer> {
    List<Team2> findAllByTeamTier(String teamTier);
}
