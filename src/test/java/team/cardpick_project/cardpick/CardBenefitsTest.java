package team.cardpick_project.cardpick;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

public class CardBenefitsTest extends AcceptanceTest {
    @Test
    void cardBenefitsGetTest() {
        RestAssured
                .given().log().all()
                .pathParam("cardId", 1L)
                .when()
                .get("/api/card-benefits/{cardId}")
                .then().log().all()
                .statusCode(200);
    }
}
