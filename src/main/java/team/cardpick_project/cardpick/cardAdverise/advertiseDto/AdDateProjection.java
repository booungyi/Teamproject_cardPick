package team.cardpick_project.cardpick.cardAdverise.advertiseDto;

import java.time.LocalDate;

public record AdDateProjection(
        LocalDate startDate,
        LocalDate endDate) {
}
