package team.cardpick_project.cardpick.cardpick.cardpickDto;

import java.time.LocalDateTime;

public record CreateAdRequest(Long cardpickId,
                              LocalDateTime start,
                              LocalDateTime end) {
}
