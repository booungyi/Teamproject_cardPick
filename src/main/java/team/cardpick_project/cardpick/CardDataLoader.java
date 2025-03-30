package team.cardpick_project.cardpick;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import team.cardpick_project.cardpick.cardbenefits.CardBenefitsService;
import team.cardpick_project.cardpick.card.service.CardPickService;

@Component
@RequiredArgsConstructor
public class CardDataLoader implements CommandLineRunner {

    private final CardPickService cardService;
    private final CardBenefitsService cardBenefitsService;

    @Override
    public void run(String... args) {
        cardService.saveCardsFromCSV("card_info.csv");
        cardBenefitsService.saveCardsBenefitsFromCSV("card_benefits_info.csv");
    }
}
