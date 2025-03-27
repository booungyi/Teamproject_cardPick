package team.cardpick_project.cardpick.cardpick.cardpickDto;

import java.time.LocalDateTime;

public record CreateAdTermRequest(
        Long adCardId,
        LocalDateTime start,
        LocalDateTime end) {
}
