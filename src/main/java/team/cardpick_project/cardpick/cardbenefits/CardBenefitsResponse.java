package team.cardpick_project.cardpick.cardbenefits;

import java.util.List;

public record CardBenefitsResponse (
        String cardImageUrl,
        String cardName,
        List<CardBenefitsQDto> benefits
){
}
