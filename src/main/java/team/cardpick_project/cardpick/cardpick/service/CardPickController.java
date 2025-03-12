package team.cardpick_project.cardpick.cardpick.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CardRecommendationRequest;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CardResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/card_pick")
public class CardPickController {
    private final CardPickService cardPickService;

    @GetMapping
    public List<CardResponse> getCardsByCondition(@RequestBody@Valid CardRecommendationRequest rq){
        return cardPickService.getCardsByCondition(rq);
    }
}
