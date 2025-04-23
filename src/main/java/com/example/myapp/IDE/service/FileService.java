package com.example.myapp.IDE.service;

import com.example.myapp.IDE.dto.FileCreateRequest;
import com.example.myapp.IDE.dto.FileUpdateRequest;
import com.example.myapp.IDE.entity.File;

public interface FileService {
    File createFile(FileCreateRequest request);
    File updateFile(FileUpdateRequest request);
}
