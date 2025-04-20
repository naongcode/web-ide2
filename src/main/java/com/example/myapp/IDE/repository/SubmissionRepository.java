package com.example.myapp.IDE.repository;

import com.example.myapp.IDE.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {

}