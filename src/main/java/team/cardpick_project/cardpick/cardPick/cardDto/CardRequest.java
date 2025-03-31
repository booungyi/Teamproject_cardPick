package team.cardpick_project.cardpick.cardPick.cardDto;

public record CardRequest(
        String cardName,
        String description,
        String annualFee,
        String imageUrl,
        String detailUrl
) {
}
