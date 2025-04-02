package team.cardpick_project.cardpick.cardPick.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.cardpick_project.cardpick.cardAdverise.AdQueryRepository;
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



    public List<CardResponse> getCardsByConditions(String issuer, List<String> categories) {
        List<CardResponse> cardResponse = cardDao.getCardsByConditions(issuer, categories).stream()
                .map(data -> CardResponse.toDtoFromQDto(data, false,1))
                .collect(Collectors.toList());

        // ⭐ 클릭 수 증가 로직 추가
        cardResponse.forEach(card -> cardDao.incrementClickCount(card.id()));

        LocalDateTime today = LocalDateTime.now();
        List<CardPick> activeAdCardPick = adQueryRepository.findActiveAdCard(today);

        List<CardResponse> adCardReponses = activeAdCardPick.stream()
                .map(active -> new CardResponse(
                        active.getId(),
                        active.getCardName(),
                        active.getImageUrl(),
                        active.getDetailUrl(),
                        true,
                        active.getClickCount()+1//증가된 클릭수 값
                ))
                .toList();



        return cardResponse;
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

                CardPick cardPick = new CardPick(name, description, annualFee, imageUrl, detailUrl);

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

    public Long getCountByConditions(String issuer, List<String> categories) {
        Long count = cardDao.getCountByConditions(issuer, categories);
        return count;
    }

    public List<CardResponse> getCardsByMbti(String mbti) {
        List<CardResponse> cardResponse = cardDao.getCardsByMbti(mbti).stream()
//                .map(CardResponse::toDtoFromQDto)
                .map(data -> CardResponse.toDtoFromQDto(data, false,1))
                .collect(Collectors.toList());

        // ⭐ 클릭 수 증가 로직 추가
        cardResponse.forEach(card -> cardDao.incrementClickCount(card.id()));

        // TODO: 광고 중인 카드 1개 가지고 오기 db에서
        LocalDateTime today = LocalDateTime.now();
        List<CardPick> activeAdCardPick = adQueryRepository.findActiveAdCard(today);

        // 광고 카드이면, 같은 CardResponse에 추가
        List<CardResponse> adCardReponses = activeAdCardPick.stream()
                .map(active -> new CardResponse(
                        active.getId(),
                        active.getCardName(),
                        active.getImageUrl(),
                        active.getDetailUrl(),
                        true,
                        active.getClickCount()
                ))
                .toList();



        return cardResponse;
    }
    //상세조회 카운트 하는 서비스 로직에 인기순으로 정ㄹ렬하는 함수 추가
    @Transactional
    public void incrementClickCount(Long id) {
        CardPick cardPick = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found: " + id));
        cardPick.incrementClickCount();
        getPopularCards();
    }

   //인기순으로 정렬하는 함수
    public List<CardResponse> getPopularCards() {
        return cardDao.getPopularCards().stream()
                .map(data -> CardResponse.toDtoFromQDto(data, false, data.clickCount())) // clickCount() 사용
                .toList();
    }

}
