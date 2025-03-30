package team.cardpick_project.cardpick.card.cardDto;

import java.util.List;

public record CardRecommendationRequest(
        String issuer,
        List<String> categories
) {
}
