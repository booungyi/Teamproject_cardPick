package team.cardpick_project.cardpick;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import team.cardpick_project.cardpick.cardpick.domain.CardPick;
import team.cardpick_project.cardpick.cardpick.service.CardPickService;

@Component
@RequiredArgsConstructor
public class CardDataLoader implements CommandLineRunner {

    private final CardPickService cardService;

    @Override
    public void run(String... args) {
        cardService.saveCardsFromCSV("card_info.csv");
    }
}
