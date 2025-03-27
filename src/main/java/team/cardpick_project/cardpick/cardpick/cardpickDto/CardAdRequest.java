package team.cardpick_project.cardpick.cardpick.cardpickDto;

import team.cardpick_project.cardpick.cardpick.domain.CardPick;

import java.time.LocalDateTime;
import java.util.List;

public record CardAdRequest (
        Long cardId,
        LocalDateTime startDate,
        LocalDateTime endDate
){

}