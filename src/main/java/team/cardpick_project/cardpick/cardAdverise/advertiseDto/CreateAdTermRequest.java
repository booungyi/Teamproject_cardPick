package team.cardpick_project.cardpick.cardAdverise.advertiseDto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAdTermRequest(
        @NotNull @FutureOrPresent LocalDateTime start,
        @NotNull @Future LocalDateTime end) {
}
