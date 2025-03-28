package team.cardpick_project.cardpick.ad;

import java.time.LocalDate;

public record AdResponse(
        Long id,
        Long cardPickId,
        Double budget,
        Double spentAmount,
        String status,
        LocalDate startDate,
        LocalDate endDate,
        boolean isBudgetExceeded
) {
    public AdResponse(Advertising advertising) {
        this(
                advertising.getId(),
                advertising.getCardPick().getId(),
                advertising.getBudget(),
                advertising.getSpentAmount(),
                advertising.getStatus().name(), // enum이면 name() 사용
                advertising.getStartDate(),
                advertising.getEndDate(),
                advertising. isBudgetExceeded()
        );


    }



}