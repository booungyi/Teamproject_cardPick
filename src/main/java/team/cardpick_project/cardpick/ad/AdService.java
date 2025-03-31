package team.cardpick_project.cardpick.ad;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import team.cardpick_project.cardpick.cardpick.domain.CardPick;
import team.cardpick_project.cardpick.cardpick.domain.CardPickRepository;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AdService {
    private final AdRepository adRepository;
    private final CardPickRepository cardPickRepository;

    public AdService(AdRepository adRepository, CardPickRepository cardPickRepository) {
        this.adRepository = adRepository;
        this.cardPickRepository = cardPickRepository;
    }

//    //생성
//    public void save(@Valid AdRequest request) {
//        adRepository.save(new Advertising(
//                request.cardPickId(),
//                request.budget(),
//                request.spentAmount(),
//                request.status(),
//                request.startDate(),
//                request.endDate()
//        ));
//    }
    //단일 조회
    public List<AdResponse> read(long id) {
        Advertising advertising = adRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("광고 ID " + id + "에 해당하는 광고가 없습니다."));

        return List.of(new AdResponse(advertising));
    }



    //여러개 조회
    public List<AdResponse> findAllAds() {
        List<Advertising>ads = adRepository.findAll();

        return ads.stream()
                .map(AdResponse :: new)
                .collect(Collectors.toList());

        }

   //광고 예산 소진 상태 확인
    public List<AdResponse> getBudgetStatus(Long id) {
        Advertising advertising = adRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("광고 ID " + id + "에 해당하는 광고가 없습니다."));

        boolean isBudgetExceeded = advertising.getSpentAmount() >= advertising.getBudget();

        AdStatuse status = isBudgetExceeded ? AdStatuse.INACTIVE : advertising.getStatus();



        AdResponse adResponse = new AdResponse(
                advertising.getCardPick().getId(),
                advertising.getId(),               // updatedAdvertising 대신 광고 객체에서 직접 가져오기
                advertising.getBudget(),
                advertising.getSpentAmount(),
                advertising.getStatus().name(),    // status는 Enum이므로 name() 사용
                advertising.getStartDate(),
                advertising.getEndDate(),
                isBudgetExceeded
        );

        return List.of(adResponse);
    }
    //상태별 조회
    public List<AdResponse> findAdsByStatus(AdStatuse status) {

        List<Advertising> ads = adRepository.findByStatus(status); // 상태별로 광고 조회
        return ads.stream().map(ad -> new AdResponse(ad)).collect(Collectors.toList());
    }


    @Transactional
    public AdResponse save(@Valid AdRequest request) {
        // 카드 조회
        CardPick cardPick = cardPickRepository.findById(request.cardPickId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카드입니다."));

        // 중첩 광고 개수 체크
        int overlapCount = adRepository.countByCardPickAndDateOverlap(
                cardPick,
                request.startDate(),
                request.endDate()
        );
        if (overlapCount >= 3) throw new IllegalStateException("광고 기간 중첩 3개 초과");

        // 광고 저장
        Advertising ad = adRepository.save(new Advertising(
                cardPick,
                request.budget(),
                request.spentAmount(),
                request.status(),
                request.startDate(),
                request.endDate()
        ));

        return new AdResponse(ad);
    }
//    //광고 예산수정
//    public AdResponse updateAdBudget(Long id, int budget) {
//        return null;
//    }
}
