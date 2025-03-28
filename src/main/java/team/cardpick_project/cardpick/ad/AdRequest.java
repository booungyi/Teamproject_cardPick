package team.cardpick_project.cardpick.ad;

import java.time.LocalDate;

public record AdRequest(
        Long cardPickId,
        Double budget,           // 광고 예산
        Double spentAmount,      // 소진된 예산
        AdStatuse status,           // 광고 상태 (ACTIVE, INACTIVE 등)
        LocalDate startDate,     // 광고 시작일
        LocalDate endDate      // 광고 종료일

) {
}
