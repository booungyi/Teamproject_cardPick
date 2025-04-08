package team.cardpick_project.cardpick.cardAdverise;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import team.cardpick_project.cardpick.cardAdverise.advertiseDto.*;
import team.cardpick_project.cardpick.cardPick.cardDto.ActiveResponse;
import team.cardpick_project.cardpick.cardPick.domain.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AdvertiseService {

    public final CardRepository cardRepository;
    public final AdvertiseRepository advertiseRepository;
    public final AdQueryRepository adQueryRepository;

    public AdvertiseService(CardRepository cardRepository, AdvertiseRepository advertiseRepository, AdQueryRepository adQueryRepository) {
        this.cardRepository = cardRepository;
        this.advertiseRepository = advertiseRepository;
        this.adQueryRepository = adQueryRepository;
    }

    public void create(CreateAdRequest request) {
        CardPick cardPick = cardRepository.findById(request.cardPickId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드"));
        adQueryRepository.CountExistingAds(request.start(), request.end());
        //새 광고 생성
        advertiseRepository.save(
                new Advertise(
                        cardPick,
                        request.start(),
                        request.end()
                ));
    }

    //광고 매일 자정 업데이트
    @Scheduled(cron = "0 0 0 * * *")
    public void update() {
        List<Advertise> ads = advertiseRepository.findByAdStatusIn(List.of(AdStatus.PENDING, AdStatus.ACTIVE));

        for (Advertise ad : ads) {
            ad.updateStatus();
        }
        advertiseRepository.saveAll(ads);
    }

    //광고 조회- isDelete= false인 광고만 찾아옴
    public List<ActiveResponse> findAllAD() {
        List<Advertise> advertiseList = advertiseRepository.findByIsDeletedFalse();

        return advertiseList.stream()
                .map(advertise -> new ActiveResponse(
                        advertise.getId(),
                        advertise.getCardPick().getCardName(),
                        advertise.getCardPick().getImageUrl(),
                        advertise.getCardPick().getDetailUrl()
                )).toList();
    }

    // 광고 기간 수정
    //TODO: 등록된 카드인지 확인 - 광고카드인지 확인 - pending,active인 카드만 수정
    @Transactional
    public void termUpdate(Long adCardId, CreateAdTermRequest request) {
        Advertise advertise = advertiseRepository.findById(adCardId)
                .orElseThrow(() -> new IllegalArgumentException("광고카드 아님"));
        // 광고 기간이 겹치는지 확인
        adQueryRepository.CountExistingAds(request.start(), request.end());
        // 에외 처리 없을경우는 바로 수정
        advertise.setStartDate(request.start());
        advertise.setEndDate(request.end());
    }

    public void deleteAd(Long adCardId) {
        // 광고 삭제 시, isDeleted를 true로 , Satatus 를 END 로 설정
        Advertise advertise = advertiseRepository.findById(adCardId)
                .orElseThrow(() -> new IllegalArgumentException("광고카드 아님"));
        advertise.deleted();
        advertiseRepository.save(advertise);
    }

    public List<AdResponse> findAdList() {
        return advertiseRepository.findByIsDeletedFalse().stream()
                .map(advertise -> new AdResponse(
                        advertise.getId(),
                        advertise.getCardPick().getId(),
                        advertise.getStartDate(),
                        advertise.getEndDate()
                )).toList();
    }

    //광고 예산 소진 상태 확인
    public List<BudgetResponse> getBudgetStatus(Long id) {
        Advertise advertise = advertiseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("광고 ID " + id + "에 해당하는 광고가 없습니다."));

        boolean isBudgetExceeded = advertise.getSpentAmount() >= advertise.getBudget();

        AdStatus status = isBudgetExceeded ? AdStatus.INACTIVE : advertise.getAdStatus();


        BudgetResponse budgetResponse = new BudgetResponse(
                advertise.getCardPick().getId(),
                advertise.getId(),               // updatedAdvertising 대신 광고 객체에서 직접 가져오기
                // status는 Enum이므로 name() 사용
                advertise.getStartDate(),
                advertise.getEndDate()
        );

        return List.of(budgetResponse);
    }

    public List<BudgetResponse> findAdsByStatus(AdStatus status) {
        List<Advertise> ads = advertiseRepository.findByAdStatusIn(List.of(status)); // 상태별로 광고 조회
        return ads.stream().map(ad -> new BudgetResponse(ad)).collect(Collectors.toList());
    }


    //여러개 조회
    public List<BudgetResponse> findAllAds() {
        List<Advertise> ads = advertiseRepository.findAll();

        return ads.stream()
                .map(BudgetResponse::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public BudgetResponse save(@Valid BudgetRequest request) {
        // 카드 조회
        CardPick cardPick = cardRepository.findById(request.cardPickId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카드입니다."));

        // 중첩 광고 개수 체크
        int overlapCount = advertiseRepository.countByCardPickAndDateOverlap(
                cardPick,
                request.startDate(),
                request.endDate()
        );
        if (overlapCount >= 3) throw new IllegalStateException("광고 기간 중첩 3개 초과");

        // 광고 저장 (status는 무조건 ACTIVE로 고정)
        Advertise ad = advertiseRepository.save(new Advertise(
                request.startDate(),
                request.endDate(),
                AdStatus.ACTIVE, // ✅ 여기 고정!
                cardPick,
                request.budget(),
                request.spentAmount()
        ));

        return new BudgetResponse(ad);
    }
    //광고 클릭 할떄마다 예산 소진
    public void handleAdClick(Long advertiseId) {
        Advertise ad = advertiseRepository.findById(advertiseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 광고를 찾을 수 없습니다."));

        ad.addSpentAmount(1000);  // 클릭당 1000원 차감만 명확히

        advertiseRepository.save(ad);
    }
    }

