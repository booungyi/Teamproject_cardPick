package team.cardpick_project.cardpick.cardAdverise.advertiseDto;

import java.time.LocalDateTime;

public record AdResponse(Long id,
                         Long cardPickId,
                         LocalDateTime start,
                         LocalDateTime end) {
}
