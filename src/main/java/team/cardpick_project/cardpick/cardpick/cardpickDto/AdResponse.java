package team.cardpick_project.cardpick.cardpick.cardpickDto;

import java.time.LocalDateTime;

public record AdResponse(Long id,
                         LocalDateTime start,
                         LocalDateTime end) {
}
