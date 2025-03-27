package team.cardpick_project.cardpick.cardpick.service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CardAdRequest;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CardAdResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CardAdController {

    private final CardAdService cardAdService;

    @PostMapping("api/cardpicks/cardAds")
    public void createCardAd(@RequestBody CardAdRequest request) {
        cardAdService.create(request);
    }

    @GetMapping("api/cardpicks/cardAds")
    public List<CardAdResponse> getCardAd() {
        return cardAdService.getCardAdById();
    }

    @PostMapping("api/cardpicks/cardAds")
    public void updateCardAd(@RequestBody CardAdUpdateRequest request) {
        cardAdService.updateCardAdById(request);
    }

    @DeleteMapping("/api/cardpicks/cardAds")
    public void deleteCardAdByCardId(@PathVariable Long cardId) {
        cardAdService.deleteCardAdById(cardId);
    }

}
