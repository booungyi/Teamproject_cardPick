package team.cardpick_project.cardpick.cardAdverise.advertiseDto;

import java.time.LocalDateTime;

public record CreateAdRequest(Long cardpickId,
                              LocalDateTime start,
                              LocalDateTime end) {
}
