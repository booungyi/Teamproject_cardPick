package team.cardpick_project.cardpick.cardpick.cardpickDto;

import team.cardpick_project.cardpick.cardpick.domain.CardAd;

import java.util.List;

public record CardAdResponse (
        List<CardResponse> responses
){
}
