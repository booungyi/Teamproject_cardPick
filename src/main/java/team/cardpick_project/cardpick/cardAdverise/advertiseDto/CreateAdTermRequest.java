package team.cardpick_project.cardpick.cardAdverise.advertiseDto;

import java.time.LocalDateTime;

public record CreateAdTermRequest(
        LocalDateTime start,
        LocalDateTime end) {
}
