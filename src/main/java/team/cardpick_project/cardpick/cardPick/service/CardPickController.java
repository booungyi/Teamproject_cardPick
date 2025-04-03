package team.cardpick_project.cardpick.cardPick.service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.cardpick_project.cardpick.cardPick.cardDto.CardResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/card_picks")
public class CardPickController {

    private final CardPickService cardPickService;

    //선택된 조건에 맞는 카드 추천 인기카드 카운트 메서드 추가
    @GetMapping("/conditions")
    public List<CardResponse> getCardsByConditions(
            @RequestParam(required = false) String issuer,
            @RequestParam List<String> categories) {
        return cardPickService.getCardsByConditions(issuer, categories);
    }

    //현재 선택된 조건에 맞는 카드 개수
    //issuer 잠시보류
    @GetMapping("/conditions/count")
    public Long getCountByConditions(
//            @RequestParam(required = false) String issuer,
            @RequestParam(required = false) List<String> categories) {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        return cardPickService.getCountByConditions(categories);
    }

    @GetMapping("/mbti")
    public List<CardResponse> getCardsByMbti(@RequestParam String mbti) {
        return cardPickService.getCardsByMbti(mbti);
    }

    @PatchMapping("/{cardId}")
    public void incrementClickCount(@PathVariable Long cardId) {
        cardPickService.incrementClickCount(cardId);
    }


    //인기순으로 주는 api
    @GetMapping("/popular")
    public List<CardResponse> popularCard(){
        return cardPickService.getPopularCards();

    }
}
