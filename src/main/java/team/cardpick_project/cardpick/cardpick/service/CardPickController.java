package team.cardpick_project.cardpick.cardpick.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CardMbtiResponse;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CardRecommendationRequest;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CardResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/card_picks")
public class CardPickController {
    private final CardPickService cardPickService;

    //선택된 조건에 맞는 카드 추천
    @GetMapping("/conditions")
    public List<CardResponse> getCardsByConditions(@RequestBody@Valid CardRecommendationRequest rq){
        return cardPickService.getCardsByConditions(rq);
    }
    //현재 선택된 조건에 맞는 카드 개수
    @GetMapping("/conditions/count")
    public Integer getCountByConditions(@RequestBody@Valid CardRecommendationRequest rq){
        return cardPickService.getCountByConditions(rq);
    }
    //성향에 맞는 카드 추천
    @GetMapping("/mbti")
    public List<CardResponse> getCardsByMbti(@RequestBody@Valid CardMbtiResponse rq){
        return cardPickService.getCardsByMbti(rq);
    }
}
