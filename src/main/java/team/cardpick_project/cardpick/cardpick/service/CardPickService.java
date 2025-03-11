package team.cardpick_project.cardpick.cardpick.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.cardpick_project.cardpick.cardpick.domain.CardPick;
import team.cardpick_project.cardpick.cardpick.domain.CardPickRepository;
import org.apache.commons.csv.*;
import team.cardpick_project.cardpick.cardpick.domain.Category;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CardPickService {
    private final CardPickRepository cardPickRepository;

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

                // parsed_desc 문자열을 리스트로 변환
                String parsedDescStr = record.get("parsed_desc").replace("[", "").replace("]", "").replace("'", "");
                List<Category> parsedDesc = Arrays.asList(parsedDescStr.split(", ")).stream()
                        .map(Category::fromString)
                        .toList();

                CardPick cardPick = new CardPick(name, parsedDesc, description, annualFee, imageUrl, detailUrl);
                cardPickRepository.save(cardPick);
            }

            System.out.println("CSV 데이터가 성공적으로 저장되었습니다!");

        } catch (IOException e) {
            System.err.println("CSV 파일을 읽는 중 오류 발생: " + e.getMessage());
        }
    }
}
