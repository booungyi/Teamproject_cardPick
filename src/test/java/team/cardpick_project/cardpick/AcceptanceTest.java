package team.cardpick_project.cardpick;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import team.cardpick_project.cardpick.cardPick.service.CardPickService;
import team.cardpick_project.cardpick.cardbenefits.CardBenefitsService;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AcceptanceTest {

    @Autowired
    private CardPickService cardPickService;

    @Autowired
    private CardBenefitsService cardBenefitsService;

//    @Autowired
//    public AcceptanceTest(CardPickService cardPickService, CardBenefitsService cardBenefitsService){
//        this.cardBenefitsService = cardBenefitsService;
//        this.cardPickService = cardPickService;
//    }
    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        cardPickService.saveCardsFromCSV("card_info.csv");
        cardBenefitsService.saveCardsBenefitsFromCSV("card_benefits_info.csv");
    }
}
