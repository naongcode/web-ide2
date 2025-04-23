package com.example.myapp.question;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestDto {

    @NotBlank(message = "작성자 ID는 필수입니다.")
    @JsonProperty("user_id")
    private String userId;  // 프론트에서는 user_id로 보냄

    @NotNull(message = "Team ID는 필수입니다.")
    @JsonProperty("team_id")
    private Long teamId;

    @NotNull(message = "문제 이름은 필수입니다.")
    @Size(min = 1, max = 100, message = "문제 이름은 1자 이상, 100자 이하로 작성해야 합니다.")
    @JsonProperty("quest_name")
    private String questName;

    @NotNull(message = "문제 시작 날짜는 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("quest_start")
    private LocalDate questStart;

//    @NotNull(message = "문제 마감 날짜는 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("quest_due")
    private LocalDate questDue;

    @Size(max = 200, message = "링크는 최대 200자까지 입력할 수 있습니다.")
    @JsonProperty("quest_link")
    private String questLink;

    public String getCreatorId() {
        return userId; // 내부적으로 creatorId로 사용
    }
}
