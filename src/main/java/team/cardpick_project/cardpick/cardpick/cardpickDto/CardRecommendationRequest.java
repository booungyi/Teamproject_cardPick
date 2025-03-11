package team.cardpick_project.cardpick.cardpick.cardpickDto;

import team.cardpick_project.cardpick.cardpick.Category;

public record CardRecommendationRequest(
        String issuer,
        Category category
) {
}
