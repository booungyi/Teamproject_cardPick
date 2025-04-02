package team.cardpick_project.cardpick.cardAdverise;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import team.cardpick_project.cardpick.cardAdverise.advertiseDto.AdResponse;
import team.cardpick_project.cardpick.cardAdverise.advertiseDto.CreateAdRequest;
import team.cardpick_project.cardpick.cardAdverise.advertiseDto.CreateAdTermRequest;
import team.cardpick_project.cardpick.cardPick.cardDto.ActiveResponse;
import team.cardpick_project.cardpick.cardPick.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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
//    public void termUpdate(Long adCardId, CreateAdTermRequest request) {
//        Advertise advertise = advertiseRepository.findById(adCardId)
//                .orElseThrow(() -> new IllegalArgumentException("광고카드 아님"));
//
//        // 광고 기간 겹치는지 리스트 확인
//        List<Advertise> overlappingAds = adQueryRepository.findOverlappingAdDates(request.start(),request.end());
//
//        // 현재 수정하려는 광고는 제외
//        overlappingAds = overlappingAds.stream()
//                .filter(ad -> !ad.getId().equals(adCardId))
//                .toList();
//
//        //날짜별로 광고 수 계산
//        Map<LocalDate, Long> adCountMap = new HashMap<>();
//
//        for (Advertise ad: overlappingAds){
//            LocalDate adStart = ad.getStartDate().toLocalDate();
//            LocalDate adEnd = ad.getEndDate().toLocalDate();
//
//            // 광고가 시작되는 날부터 끝나는 날까지의 각 날짜에 대해
//            for (LocalDate currentDate = adStart; !currentDate.isAfter(adEnd); currentDate = currentDate.plusDays(1)) {
//                adCountMap.put(currentDate, adCountMap.getOrDefault(currentDate, 0L) + 1);
//            }
//        }
//
//        // 수정하려는 기간에 대해 날짜별로 확인
//        LocalDate requestStart = request.start().toLocalDate();
//        LocalDate requestEnd = request.end().toLocalDate();
//
//        for (LocalDate currentDate = requestStart; !currentDate.isAfter(requestEnd); currentDate = currentDate.plusDays(1)) {
//            if (adCountMap.getOrDefault(currentDate, 0L) >= 5) {
//                throw new IllegalArgumentException(currentDate + " 날짜에 광고는 이미 최대 5개가 등록되어 있습니다.");
//            }
//        }
//        // 에외 처리 없을경우는 바로 수정
//        advertise.setStartDate(request.start());
//        advertise.setEndDate(request.end());
//    }
    public void termUpdate(Long adCardId, CreateAdTermRequest request) {
        Advertise advertise = advertiseRepository.findById(adCardId)
                .orElseThrow(() -> new IllegalArgumentException("광고카드 아님"));

        LocalDate originalStart = advertise.getStartDate().toLocalDate();
        LocalDate originalEnd = advertise.getEndDate().toLocalDate();
        LocalDate newStart = request.start().toLocalDate();
        LocalDate newEnd = request.end().toLocalDate();

        // 새로 추가되는 날짜 범위만 계산
        List<LocalDate> datesToCheck = new ArrayList<>();

        // 시작일이 당겨졌으면 그 날짜들 추가
        if (newStart.isBefore(originalStart)) {
            for (LocalDate date = newStart; date.isBefore(originalStart); date = date.plusDays(1)) {
                datesToCheck.add(date);
            }
        }

        // 종료일이 연장됐으면 그 날짜들 추가
        if (newEnd.isAfter(originalEnd)) {
            for (LocalDate date = originalEnd.plusDays(1); !date.isAfter(newEnd); date = date.plusDays(1)) {
                datesToCheck.add(date);
            }
        }

        // 추가된 날짜가 없으면 바로 수정
        if (datesToCheck.isEmpty()) {
            advertise.setStartDate(request.start());
            advertise.setEndDate(request.end());
            return;
        }

        // 광고 기간 겹치는지 확인
        List<Advertise> overlappingAds = adQueryRepository.findOverlappingAdDates(
                LocalDateTime.of(Collections.min(datesToCheck), LocalTime.MIN),
                LocalDateTime.of(Collections.max(datesToCheck), LocalTime.MAX)
        );

        // 현재 수정하려는 광고는 제외
        overlappingAds = overlappingAds.stream()
                .filter(ad -> !ad.getId().equals(adCardId))
                .toList();

        // 날짜별로 광고 수 계산하기
        Map<LocalDate, Long> adCountMap = new HashMap<>();

        for (Advertise ad : overlappingAds) {
            LocalDate adStart = ad.getStartDate().toLocalDate();
            LocalDate adEnd = ad.getEndDate().toLocalDate();

            for (LocalDate date : datesToCheck) {
                if (!date.isBefore(adStart) && !date.isAfter(adEnd)) {
                    adCountMap.put(date, adCountMap.getOrDefault(date, 0L) + 1);
                }
            }
        }

        // 추가된 날짜에 대해서만 광고 수 확인
        for (LocalDate date : datesToCheck) {
            if (adCountMap.getOrDefault(date, 0L) >= 5) {
                throw new IllegalArgumentException(date + " 날짜에 광고는 이미 최대 5개가 등록되어 있습니다.");
            }
        }

        // 예외 처리 없을 경우는 바로 수정
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
}
