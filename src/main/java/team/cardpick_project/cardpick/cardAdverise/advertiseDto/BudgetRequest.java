package team.cardpick_project.cardpick.cardAdverise.advertiseDto;

import team.cardpick_project.cardpick.cardAdverise.AdStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BudgetRequest(
        Long cardPickId,
        int budget,           // 광고 예산
        int spentAmount,      // 소진된 예산
        AdStatus status,           // 광고 상태 (ACTIVE, INACTIVE 등)
        LocalDateTime startDate,     // 광고 시작일
        LocalDateTime endDate      // 광고 종료일

) {
}
