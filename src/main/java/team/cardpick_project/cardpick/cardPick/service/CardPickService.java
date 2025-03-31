package team.cardpick_project.cardpick.cardPick.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.cardpick_project.cardpick.cardAdverise.AdQueryRepository;
import team.cardpick_project.cardpick.cardPick.cardDto.CardRequest;
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
                .map(data -> CardResponse.toDtoFromQDto(data, false))
                .collect(Collectors.toList());

        LocalDateTime today = LocalDateTime.now();
        List<CardPick> activeAdCardPick = adQueryRepository.findActiveAdCard(today);

        List<CardResponse> adCardReponses = activeAdCardPick.stream()
                .map(active -> new CardResponse(
                        active.getCardName(),
                        active.getImageUrl(),
                        active.getDetailUrl(),
                        true
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
                .map(data -> CardResponse.toDtoFromQDto(data, false))
                .collect(Collectors.toList());

        // TODO: 광고 중인 카드 1개 가지고 오기 db에서
        LocalDateTime today = LocalDateTime.now();
        List<CardPick> activeAdCardPick = adQueryRepository.findActiveAdCard(today);

        // 광고 카드이면, 같은 CardResponse에 추가
        List<CardResponse> adCardReponses = activeAdCardPick.stream()
                .map(active -> new CardResponse(
                        active.getCardName(),
                        active.getImageUrl(),
                        active.getDetailUrl(),
                        true
                ))
                .toList();
        return cardResponse;
    }

    public void saveCard(CardRequest cardRequest) {
        CardPick cardPick = new CardPick(
                cardRequest.cardName(),
                cardRequest.description(),
                cardRequest.annualFee(),
                cardRequest.imageUrl(),
                cardRequest.detailUrl()
        );


        cardRepository.save(cardPick);
    }

    public List<CardPick> getAllCards() {
        return cardRepository.findAll();
    }
}
