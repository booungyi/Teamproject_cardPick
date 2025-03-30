package team.cardpick_project.cardpick;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import team.cardpick_project.cardpick.cardAdverise.advertiseDto.AdResponse;
import team.cardpick_project.cardpick.cardAdverise.advertiseDto.CreateAdRequest;
import team.cardpick_project.cardpick.cardAdverise.advertiseDto.CreateAdTermRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AdTest {

    @Test
    void 광고_생성() {
        AdResponse 광고 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new CreateAdRequest(
                        1L,
                        LocalDateTime.parse("2025-03-26T00:00:00"),
                        LocalDateTime.parse("2025-04-25T00:00:00")))
                .when()
                .post("/ad") // POST /members 요청
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(AdResponse.class);

        assertThat(광고).isNotNull();
    }

    @Test
    void 광고_조회() {
        //조회
        RestAssured
                .given()
                .when()
                .get("/ad") // 서버로 GET /products 요청
                .then()
                .statusCode(200);
    }

    @Test
    void 광고_기간수정() {
        AdResponse 광고 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new CreateAdRequest(
                        1L,
                        LocalDateTime.parse("2025-03-26T00:00:00"),
                        LocalDateTime.parse("2025-04-25T00:00:00")))
                .when()
                .post("/ad") // POST /members 요청
                .then().log().all()
                .statusCode(200)
                .extract()
                .as(AdResponse.class);

        //수정
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("adCardId",1L)
                .body(new CreateAdTermRequest(
                        LocalDateTime.parse("2025-03-26T00:00:00"),
                        LocalDateTime.parse("2025-04-20T00:00:00")
                ))
                .when()
                .put("/ad/{adCardId}")
                .then().log().all()
                .statusCode(200);

        assertThat(광고).isNotNull();
    }
}
