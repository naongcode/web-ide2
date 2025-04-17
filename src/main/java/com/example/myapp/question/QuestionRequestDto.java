package com.example.myapp.question;

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
    private String userId;  // 프론트에서는 userId로 보냄

    @NotNull(message = "Team ID는 필수입니다.")
    private Long teamId;

    @NotNull(message = "문제 이름은 필수입니다.")
    @Size(min = 1, max = 100, message = "문제 이름은 1자 이상, 100자 이하로 작성해야 합니다.")
    private String questName;

    @NotNull(message = "문제 시작 날짜는 필수입니다.")
    private LocalDate questStart;

    @NotNull(message = "문제 마감 날짜는 필수입니다.")
    private LocalDate questDue;

    @Size(max = 200, message = "링크는 최대 200자까지 입력할 수 있습니다.")
    private String questLink;

    public String getCreatorId() {
        return userId; // userId는 creatorId로 사용됨
    }


}
