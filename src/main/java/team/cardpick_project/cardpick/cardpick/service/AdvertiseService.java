package team.cardpick_project.cardpick.cardpick.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import team.cardpick_project.cardpick.cardpick.cardpickDto.AdResponse;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CreateAdRequest;
import team.cardpick_project.cardpick.cardpick.domain.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdvertiseService {

    public final CardPickRepository cardPickRepository;
    public final AdvertiseRepository advertiseRepository;
    public final AdQueryRepository adQueryRepository;

    public AdvertiseService(CardPickRepository cardPickRepository, AdvertiseRepository advertiseRepository, AdQueryRepository adQueryRepository) {
        this.cardPickRepository = cardPickRepository;
        this.advertiseRepository = advertiseRepository;
        this.adQueryRepository = adQueryRepository;
    }

    public AdResponse create(CreateAdRequest request) {
        CardPick cardPick =
                cardPickRepository.findById(request.cardpickId())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드"));

        //겹치는 광고 개수 확인
        int existingAdCount = adQueryRepository.CountExistingAds(cardPick, request.start(), request.end());
        if (existingAdCount >= 3) {
            throw new IllegalArgumentException("최대 3개의 광고만 등록 가능");
        }

        //새 광고 생성
        Advertise advertise = new Advertise(
                request.start(),
                request.end(),
                cardPick
        );
        advertiseRepository.save(advertise);

        return new AdResponse(
                advertise.getId(),
                advertise.getStartDate(),
                advertise.getEndDate()
        );
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


}
