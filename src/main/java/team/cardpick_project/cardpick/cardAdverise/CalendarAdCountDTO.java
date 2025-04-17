package team.cardpick_project.cardpick.cardAdverise;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CalendarAdCountDTO(
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
