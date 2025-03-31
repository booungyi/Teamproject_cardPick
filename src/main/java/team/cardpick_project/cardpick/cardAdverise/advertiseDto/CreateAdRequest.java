package team.cardpick_project.cardpick.cardAdverise.advertiseDto;

import java.time.LocalDateTime;

public record CreateAdRequest(Long cardPickId,
                              LocalDateTime start,
                              LocalDateTime end) {
}
