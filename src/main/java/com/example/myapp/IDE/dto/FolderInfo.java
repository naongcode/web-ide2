package com.example.myapp.IDE.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class FolderInfo {
    private Long folder_id;
    private String folder_name;
    private Long parent_id;
    private List<FileInfo> files;

}