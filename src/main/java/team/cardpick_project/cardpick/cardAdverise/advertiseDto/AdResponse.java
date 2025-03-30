package team.cardpick_project.cardpick.cardAdverise.advertiseDto;

import java.time.LocalDateTime;

public record AdResponse(Long id,
                         LocalDateTime start,
                         LocalDateTime end) {
}
