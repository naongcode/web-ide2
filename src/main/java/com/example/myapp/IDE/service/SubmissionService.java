package com.example.myapp.IDE.service;

import com.example.myapp.IDE.dto.SubmissionRequest;
import com.example.myapp.IDE.dto.SubmissionResponse;

// 인터페이스
public interface SubmissionService {
    SubmissionResponse submit(SubmissionRequest request);
}