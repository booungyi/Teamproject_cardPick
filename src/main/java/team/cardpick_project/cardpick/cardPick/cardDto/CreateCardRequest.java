package team.cardpick_project.cardpick.cardPick.cardDto;

public record CreateCardRequest(
        String cardName,
        String description,
        String annualFee,
        String imageUrl,
        String detailUrl
) {
}
