package team.cardpick_project.cardpick.cardPick.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.cardpick_project.cardpick.cardAdverise.AdQueryRepository;
import team.cardpick_project.cardpick.cardAdverise.Advertise;
import team.cardpick_project.cardpick.cardAdverise.AdvertiseRepository;
import team.cardpick_project.cardpick.cardPick.cardDto.CardResponse;
import team.cardpick_project.cardpick.cardPick.domain.*;
import org.apache.commons.csv.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardPickService {

    private final CardRepository cardRepository;
    private final CardDao cardDao;
    private final AdQueryRepository adQueryRepository;
    private final AdvertiseRepository advertiseRepository;

    public List<CardResponse> getCardsByConditions(List<String> categories) {
        List<CardResponse> cardResponse = cardDao.getCardsByConditions(categories).stream()
                .map(data -> CardResponse.toDtoFromQDto(data, false, 1))
                .collect(Collectors.toList());

        //일반 카드 이름 집합
        Set<String> normalNames = cardResponse.stream()
                .map(CardResponse::cardName)
                .collect(Collectors.toSet());

        //활성화된 광고 조회(중복 이름 제외)
        LocalDateTime today = LocalDateTime.now();
        List<CardPick> activeAdCardPick = adQueryRepository.findActiveAdCard(today,normalNames);

        // 광고 데이터 정상적으로 가져와지는지 확인
        System.out.println("조회된 광고 카드 개수: " + activeAdCardPick.size());
        activeAdCardPick.forEach(ad -> System.out.println("광고 카드: " + ad.getCardName()));

        List<CardResponse> adCards = activeAdCardPick.stream()
                .map(ad -> new CardResponse(
                        ad.getId(),
                        ad.getCardName(),
                        ad.getImageUrl(),
                        ad.getDetailUrl(),
                        true,
                        ad.getClickCount() + 1 //증가된 클릭수 값
                ))
                .toList();

        //adCards를 cardResponse에 추가하지 않는 문제
        // 광고 카드와 일반 카드를 합침 + 무작위
        List<CardResponse> combineList = new ArrayList<>(cardResponse);
        combineList.addAll(adCards);
        Collections.shuffle(combineList);

        // 최종 반환되는 카드 목록 확인
        System.out.println("최종 반환 카드 개수: " + combineList.size());
        return combineList;
    }

    @Transactional
    public void saveCardsFromCSV(String filePath) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            for (CSVRecord record : csvParser) {
                String name = record.get("name");
                String description = record.get("description");
                String annualFee = record.get("annual_fee");
                String tagInfo = record.get("tag_info");
                String rewards = record.get("rewards");
                String applyLink = record.get("apply_link");
                String imageUrl = record.get("image_url");
                String detailUrl = record.get("detail_url");

                CardPick cardPick = new CardPick(name, description, annualFee, applyLink, imageUrl, detailUrl);

                // parsed_desc 문자열을 리스트로 변환
                String parsedDescStr = record.get("parsed_desc").replace("[", "").replace("]", "").replace("'", "");
                List<CardCategory> parsedDesc = Arrays.asList(parsedDescStr.split(", ")).stream()
                        .map(category -> new CardCategory(cardPick, Category.fromString(category)))
                        .toList();
                cardPick.addCategory(parsedDesc);
                cardRepository.save(cardPick);
            }

            System.out.println("CSV 데이터가 성공적으로 저장되었습니다!");

        } catch (IOException e) {
            System.err.println("CSV 파일을 읽는 중 오류 발생: " + e.getMessage());
        }
    }

    public Long getCountByConditions(List<String> categories) {
        Long count = cardDao.getCountByConditions(categories);
        return count;
    }

    public List<CardResponse> getCardsByMbti(String mbti) {
        List<CardResponse> cardResponse = cardDao.getCardsByMbti(mbti).stream()
//                .map(CardResponse::toDtoFromQDto)
                .map(data -> CardResponse.toDtoFromQDto(data, false, 1))
                .collect(Collectors.toList());

        // TODO: 광고 중인 카드 1개 가지고 오기 db에서
        //일반 카드 이름 집합
        Set<String> normalNames = cardResponse.stream()
                .map(CardResponse::cardName)
                .collect(Collectors.toSet());

        //활성화된 광고 조회(중복 이름 제외)
        LocalDateTime today = LocalDateTime.now();
        List<CardPick> activeAdCardPick = adQueryRepository.findActiveAdCard(today,normalNames);

        // 광고 데이터 정상적으로 가져와지는지 확인
        System.out.println("조회된 광고 카드 개수: " + activeAdCardPick.size());
        activeAdCardPick.forEach(ad -> System.out.println("광고 카드: " + ad.getCardName()));

        List<CardResponse> adCards = activeAdCardPick.stream()
                .map(ad -> new CardResponse(
                        ad.getId(),
                        ad.getCardName(),
                        ad.getImageUrl(),
                        ad.getDetailUrl(),
                        true,
                        ad.getClickCount() + 1 //증가된 클릭수 값
                ))
                .toList();

        //adCards를 cardResponse에 추가하지 않는 문제
        // 광고 카드와 일반 카드를 합침 + 무작위
        List<CardResponse> combineList = new ArrayList<>(cardResponse);
        combineList.addAll(adCards);
        Collections.shuffle(combineList);

        // 최종 반환되는 카드 목록 확인
        System.out.println("최종 반환 카드 개수: " + combineList.size());
        return combineList;
    }

    //상세조회 카운트 하는 서비스 로직에 인기순으로 정ㄹ렬하는 함수 추가
    @Transactional
    public void incrementClickCount(Long id) {
        CardPick cardPick = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found: " + id));
        cardPick.incrementClickCount();
//        getPopularCards();
    }

    //인기순으로 정렬하는 함수
    public List<CardResponse> getPopularCards() {
        return cardDao.getPopularCards().stream()
                .map(data -> CardResponse.toDtoFromQDto(data, false, data.clickCount())) // clickCount() 사용
                .toList();
    }

}
