package com.example.myapp.IDE.File;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class FileUpdateResponse {

    private Date updatedAt;     // 수정된 시각
}
