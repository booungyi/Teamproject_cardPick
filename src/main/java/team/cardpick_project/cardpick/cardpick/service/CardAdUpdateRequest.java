package team.cardpick_project.cardpick.cardpick.service;

import java.time.LocalDateTime;

public record CardAdUpdateRequest(
        Long cardId,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}