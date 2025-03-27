package team.cardpick_project.cardpick.cardpick.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CardAdRequest;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CardAdResponse;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CardResponse;
import team.cardpick_project.cardpick.cardpick.domain.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardAdService {
    private final CardAdDao cardAdDao;
    private final CardAdRepository cardAdRepository;
    private final CardPickRepository cardPickRepository;

    public void create(CardAdRequest request) {
        //getReferenceByID 는 실제 엔티티 객체를 바로 조회하지않고 실제 데이터가 필요할떄까지 데이터 베이스조회를 미룸
        CardPick card = cardPickRepository.findById(request.cardId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카드가 없습니다."));

        List<CardPick> cardPicks = List.of(card);
        //List 로 감싸서 전달

        cardAdRepository.save(new CardAd(
                        cardPicks,
                        request.startDate(),
                        request.endDate())
        );
    }

    public List<CardAdResponse> getCardAdById() {
        List<Long> activeIds = cardAdDao.getCards();// QueryDSL로 ID 리스트 조회

        if (activeIds.isEmpty()) {
            return List.of(); // 활성 광고가 없을 경우 빈 리스트 반환
        }
        List<CardAd> cardAds = cardAdRepository.findAllById(activeIds);

        return cardAds.stream().map(n -> new CardAdResponse(
                n.getCardPicks().stream()
                        .map(cardPick -> new CardResponse(
                                cardPick.getCardName(),
                                cardPick.getImageUrl(),
                                cardPick.getDetailUrl()
                        )).collect(Collectors.toList())
        )).collect(Collectors.toList());

//        return cardAdDao.getCards().stream()
//                .map(n -> new CardAd(n.getCardPicks()
//                ))
//                .toList();
    }


    @Transactional
    public void updateCardAdById(CardAdUpdateRequest request) {
//        Long oneCard = cardAdDao.getCardOne(request.cardId());// QueryDsl 로 ID 리스트 조회

        CardAd cardAd = cardAdRepository.findById(request.cardId()).orElseThrow(() ->
                new IllegalArgumentException("해당 광고가 없습니다."));

        cardAd.setStartDate(request.startDate());
        cardAd.setEndDate(request.endDate());
    }

    public void deleteCardAdById(Long cardId) {
        CardAd cardAd = cardAdRepository
                .findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 광고가 없습니다."));
        cardAdRepository.delete(cardAd);
    }
}
