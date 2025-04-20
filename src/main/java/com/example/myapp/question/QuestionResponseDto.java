package com.example.myapp.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class QuestionResponseDto {

    @JsonProperty("quest_link")
    private String questLink;  // 필드명 카멜케이스로 변경

    @JsonProperty("team_id")
    private Long teamId;  // 필드명 카멜케이스로 변경

    @JsonProperty("creator_id")
    private String creatorId;  // 필드명 카멜케이스로 변경

    @JsonProperty("quest_id")
    private Long questId;  // 필드명 카멜케이스로 변경

    @JsonProperty("quest_name")
    private String questName;  // 필드명 카멜케이스로 변경

    @JsonProperty("quest_start")
    private LocalDate questStart;  // 필드명 카멜케이스로 변경

    @JsonProperty("quest_due")
    private LocalDate questDue;  // 필드명 카멜케이스로 변경

    @JsonProperty("quest_status")
    private String questStatus;  // 필드명 카멜케이스로 변경

    // 오늘 날짜와 마감 날짜를 비교하여 questStatus 설정
    public void setQuestStatus() {
        LocalDate today = LocalDate.now();
        if (questDue.isBefore(today)) {
            this.questStatus = QuestStatus.COMPLETE.name();  // Enum 값을 String으로 변환
        } else if (questStart.isBefore(today) && questDue.isAfter(today)) {
            this.questStatus = QuestStatus.ONGOING.name();
        } else {
            this.questStatus = QuestStatus.READY.name();
        }
    }

    // 추가적인 생성자 및 필요한 메서드
    public QuestionResponseDto(Long questId, String questName, String creatorId, Long teamId,
                               LocalDate questStart, LocalDate questDue, String questLink) {
        this.questId = questId;
        this.teamId = teamId;
        this.questName = questName;
        this.creatorId = creatorId;
        this.questStart = questStart;
        this.questDue = questDue;
        this.questLink = questLink;
        setQuestStatus();  // 상태 자동 설정
    }
}
