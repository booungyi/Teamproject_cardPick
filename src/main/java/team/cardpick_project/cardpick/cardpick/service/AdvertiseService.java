package team.cardpick_project.cardpick.cardpick.service;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import team.cardpick_project.cardpick.cardpick.cardpickDto.ActiveResponse;
import team.cardpick_project.cardpick.cardpick.cardpickDto.AdResponse;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CreateAdRequest;
import team.cardpick_project.cardpick.cardpick.cardpickDto.CreateAdTermRequest;
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

    public void create(CreateAdRequest request) {
        CardPick cardPick = cardPickRepository.findById(request.cardpickId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드"));

        //새 광고 생성
        advertiseRepository.save(
                new Advertise(
                        cardPick.getId(),
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
}
