package team.cardpick_project.cardpick.cardAdverise.advertiseDto;

import team.cardpick_project.cardpick.cardAdverise.Advertise;

import java.time.LocalDateTime;

public record BudgetResponse(Long id,
                             Long cardPickId,
                             LocalDateTime startDate,
                             LocalDateTime endDate
) {
    public BudgetResponse(Advertise advertise) {
        this(
                advertise.getId(),
                advertise.getCardPick().getId(),
                // enum이면 name() 사용
                advertise.getStartDate(),
                advertise.getEndDate()
        );

    }}
