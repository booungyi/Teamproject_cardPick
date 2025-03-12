package team.cardpick_project.cardpick;
import io.restassured.RestAssured;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }
}
