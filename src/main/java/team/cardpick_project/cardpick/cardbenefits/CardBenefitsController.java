package team.cardpick_project.cardpick.cardbenefits;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardBenefitsController {
    private final CardBenefitsService cardBenefitsService;

    //상세정보는 하나씩만 조회됨
    @GetMapping("/api/card-benefits/{cardId}")
    public CardBenefitsResponse getCardBenefits(@PathVariable Long cardId) {
        return cardBenefitsService.getCardxBenefits(cardId);
    }

}
