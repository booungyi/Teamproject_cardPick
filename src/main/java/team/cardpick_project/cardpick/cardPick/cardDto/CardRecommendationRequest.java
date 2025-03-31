package team.cardpick_project.cardpick.cardPick.cardDto;

import java.util.List;

public record CardRecommendationRequest(
        String issuer,
        List<String> categories
) {
}
