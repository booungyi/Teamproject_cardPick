package team.cardpick_project.cardpick.cardpick.cardpickDto;

public record CardResponse(
        String cardName,
        String imageUrl,
        String detailUrl,
        boolean isAdCard
) {
    public static CardResponse toDtoFromQDto(CardResponseQDto qDto, boolean isAdCard){
        return new CardResponse(qDto.cardName(), qDto.imageUrl(),qDto.detailUrl(),isAdCard);
    }

}
