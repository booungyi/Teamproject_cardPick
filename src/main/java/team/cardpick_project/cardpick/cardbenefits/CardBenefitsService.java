package team.cardpick_project.cardpick.cardbenefits;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import team.cardpick_project.cardpick.cardAdverise.AdStatus;
import team.cardpick_project.cardpick.cardAdverise.Advertise;
import team.cardpick_project.cardpick.cardPick.domain.CardPick;
import team.cardpick_project.cardpick.cardPick.domain.CardRepository;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

    @Service
    @RequiredArgsConstructor
    public class CardBenefitsService {
        private final CardBenefitsRepository cardBenefitsRepository;
        private final CardRepository cardRepository;
        private final CardBenefitsDao cardBenefitsDao;

        @Transactional
        public void saveCardsBenefitsFromCSV(String filePath) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim());

                for (CSVRecord record : csvParser) {
                    String name = record.get("name");

                    String benefitsString = record.get("benefits").replace("'", "\"");
                    System.out.println(benefitsString);

                    ObjectMapper objectMapper = new ObjectMapper();
                    List<Map<String, String>> benefits = objectMapper.readValue(benefitsString, new TypeReference<List<Map<String, String>>>() {});

                    CardPick cardPick = cardRepository.findByCardName(name);
                    if (cardPick ==null){
                        continue;
                    }

                    for (Map<String, String> benefit : benefits) {
                        CardBenefits cardBenefits = new CardBenefits(benefit.get("benefit_name"),benefit.get("benefit_detail"), cardPick);
                        cardBenefitsRepository.save(cardBenefits);
                    }
                }

                System.out.println("CSV 데이터가 성공적으로 저장되었습니다!");

            } catch (IOException e) {
                System.err.println("CSV 파일을 읽는 중 오류 발생: " + e.getMessage());
            }
        }

        @Transactional
        public CardBenefitsResponse getCardxBenefits(Long cardId) {

            List<CardBenefitsQDto> cardBenefits = cardBenefitsDao.getcardBenefitlist(cardId);
            CardPick cardPick = cardRepository.findById(cardId).orElseThrow(
                    () -> new IllegalArgumentException("해당 카드가 없습니다."));
            Advertise advertise = cardPick.getAdvertiseList().
                    stream().
                    filter(ad -> ad.getAdStatus() == AdStatus.ACTIVE).findFirst()
                    .orElse(null);
            if (advertise != null) {
                advertise.addSpentAmount(1000);
            }

            return new CardBenefitsResponse(
                    cardPick.getCardName(),
                    cardPick.getImageUrl(),
                    cardPick.getApplyLink(),
                    cardBenefits
            );
        }
    }
