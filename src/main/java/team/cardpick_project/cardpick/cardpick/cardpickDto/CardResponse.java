package team.cardpick_project.cardpick.cardpick.cardpickDto;

public record CardResponse(
        String cardName,
        String imageUrl,
        String detailUrl
) {
    public static CardResponse toDtoFromQDto(CardResponseQDto qDto){
        return new CardResponse(qDto.cardName(), qDto.imageUrl(),qDto.detailUrl());
    }
}
