package team.cardpick_project.cardpick.cardbenefits;

import java.util.List;

public record CardBenefitsResponse (
        String cardName,
        String cardImageUrl,
        List<CardBenefitsQDto> benefits
){
}
