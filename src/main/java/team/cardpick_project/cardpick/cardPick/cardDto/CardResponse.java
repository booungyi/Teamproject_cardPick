package team.cardpick_project.cardpick.cardPick.cardDto;

public record CardResponse(
        Long id,//카드아이디
        String cardName,
        String imageUrl,
        String detailUrl,
        boolean isAdCard,
        int clickCount

) {
    public static CardResponse toDtoFromQDto(CardResponseQDto qDto, boolean isAdCard,int clickCount){
        return new CardResponse(qDto.id(),qDto.cardName(), qDto.imageUrl(),qDto.detailUrl(),isAdCard,clickCount);
    }
}
