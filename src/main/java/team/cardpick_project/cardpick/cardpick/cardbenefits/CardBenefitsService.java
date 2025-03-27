package team.cardpick_project.cardpick.cardpick.cardbenefits;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import team.cardpick_project.cardpick.cardpick.domain.CardCategory;
import team.cardpick_project.cardpick.cardpick.domain.CardPick;
import team.cardpick_project.cardpick.cardpick.domain.CardPickRepository;
import team.cardpick_project.cardpick.cardpick.domain.Category;
import team.cardpick_project.cardpick.cardpick.exception.NoExistsCardNameException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

    @Service
    @RequiredArgsConstructor
    public class CardBenefitsService {
        private final CardBenefitsRepository cardBenefitsRepository;
        private final CardPickRepository cardPickRepository;

        @Transactional
        public void saveCardsBenefitsFromCSV(String filePath) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim());

                for (CSVRecord record : csvParser) {
                    String name = record.get("name");

                    String benefitsString = record.get("benefits").replace("'", "\"");

                    ObjectMapper objectMapper = new ObjectMapper();
                    List<Map<String, String>> benefits = objectMapper.readValue(benefitsString, new TypeReference<List<Map<String, String>>>() {});

                    CardPick cardPick = cardPickRepository.findByCardName(name).orElseThrow(()->new NoExistsCardNameException("존재하지 않는 카드명"));

                    for (Map<String, String> benefit : benefits) {
                        CardBenefits cardBenefits = new CardBenefits(benefit.get("benefit_name"),benefit.get("benefit_detail"),cardPick);
                        cardBenefitsRepository.save(cardBenefits);
                    }
                }


                System.out.println("CSV 데이터가 성공적으로 저장되었습니다!");

            } catch (IOException e) {
                System.err.println("CSV 파일을 읽는 중 오류 발생: " + e.getMessage());
            }
        }
    }
