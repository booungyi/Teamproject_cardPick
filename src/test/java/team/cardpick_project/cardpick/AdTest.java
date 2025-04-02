package team.cardpick_project.cardpick;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.cardpick_project.cardpick.cardAdverise.advertiseDto.AdResponse;
import team.cardpick_project.cardpick.cardAdverise.advertiseDto.CreateAdRequest;
import team.cardpick_project.cardpick.cardAdverise.advertiseDto.CreateAdTermRequest;
import team.cardpick_project.cardpick.cardPick.cardDto.CardResponse;
import team.cardpick_project.cardpick.cardPick.domain.CardPick;
import team.cardpick_project.cardpick.cardPick.domain.CardRepository;
import team.cardpick_project.cardpick.cardPick.service.CardPickService;
import team.cardpick_project.cardpick.cardbenefits.CardBenefitsService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AdTest extends AcceptanceTest{

    @Autowired
    private CardRepository cardRepository;

    @Test
    void 광고_생성() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new CreateAdRequest(
                        1L,
                        LocalDateTime.parse("2025-03-26T00:00:00"),
                        LocalDateTime.parse("2025-04-25T00:00:00")))
                .when()
                .post("/adCard") // POST /members 요청
                .then().log().all()
                .statusCode(200);
//                .extract()
//                .as(AdResponse.class);

    }

    @Test
    void 광고_생성_및_조회() {
        // 1. 광고 생성 (POST 요청)
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new CreateAdRequest(
                        1L, // cardPickId
                        LocalDateTime.parse("2025-03-26T00:00:00"), // start date
                        LocalDateTime.parse("2025-04-25T00:00:00"))) // end date
                .when()
                .post("/adCard") // POST 요청
                .then().log().all()
                .statusCode(200); // 응답 상태 코드가 200이어야 함

        // 2. 광고 조회 (GET 요청)
        List<AdResponse> 광고리스트 = RestAssured.given().log().all()
                .when()
                .get("/adList") // 광고 목록 조회 (GET 요청)
                .then().log().all()
                .statusCode(200) // 상태 코드가 200이어야 함
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdResponse.class); // 응답 본문을 List<AdResponse>로 변환

        // 3. 조회된 광고가 null이 아님을 확인
        assertThat(광고리스트).isNotNull();
        assertThat(광고리스트).hasSize(1); // 광고 목록의 크기 확인 (1개 광고가 생성되었으므로)

        // 4. 광고의 내용 검증
        AdResponse 광고 = 광고리스트.get(0);
        assertThat(광고).isNotNull();
        assertThat(광고.id()).isEqualTo(1L); // 광고의 id가 1이어야 함
        assertThat(광고.cardPickId()).isEqualTo(1L); // 광고의 cardPickId가 1이어야 함
        assertThat(광고.start()).isEqualTo(LocalDateTime.parse("2025-03-26T00:00:00"));
        assertThat(광고.end()).isEqualTo(LocalDateTime.parse("2025-04-25T00:00:00"));
    }

    @Test
    void 광고_기간수정() {
//        AdResponse 광고 =
        // 1. 광고 생성 (POST 요청)
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new CreateAdRequest(
                        1L, // cardPickId
                        LocalDateTime.parse("2025-03-26T00:00:00"), // start date
                        LocalDateTime.parse("2026-03-25T00:00:00"))) // end date

                .when()
                .post("/adCard") // POST 요청
                .then().log().all()
                .statusCode(200); // 응답 상태 코드가 200이어야 함
//                .extract()
//                .as(AdResponse.class);

        //수정
//        AdResponse 광고 =
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("adCardId", 1L)
                .body(new CreateAdTermRequest(
                        LocalDateTime.parse("2025-03-26T00:00:00"),
                        LocalDateTime.parse("2026-01-25T00:00:00")
                ))
                .when()
                .put("/ad/{adCardId}")
                .then().log().all()
                .statusCode(200);
//                .extract().as(AdResponse.class);
//
//        assertThat(광고).isNotNull();
//        assertThat(광고).isNotNull();
        // 2. 광고 조회 (GET 요청)
        List<AdResponse> 광고리스트 = RestAssured.given().log().all()
                .when()
                .get("/adList") // 광고 목록 조회 (GET 요청)
                .then().log().all()
                .statusCode(200) // 상태 코드가 200이어야 함
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdResponse.class); // 응답 본문을 List<AdResponse>로 변환

//        // 3. 조회된 광고가 null이 아님을 확인
//        assertThat(광고리스트).isNotNull();
//        assertThat(광고리스트).hasSize(1); // 광고 목록의 크기 확인 (1개 광고가 생성되었으므로)
//
//        // 4. 광고의 내용 검증
//        AdResponse 광고 = 광고리스트.get(0);
//        assertThat(광고).isNotNull();
//        assertThat(광고.id()).isEqualTo(1L); // 광고의 id가 1이어야 함
//        assertThat(광고.cardPickId()).isEqualTo(1L); // 광고의 cardPickId가 1이어야 함
//        assertThat(광고.start()).isEqualTo(LocalDateTime.parse("2025-03-26T00:00:00"));
//        assertThat(광고.end()).isEqualTo(LocalDateTime.parse("2025-04-20T00:00:00"));


    }
}


