package team.cardpick_project.cardpick.cardpick.cardpickDto;

import java.util.List;

public record CardRecommendationRequest(
        String issuer,
        List<String> categories
) {
}
