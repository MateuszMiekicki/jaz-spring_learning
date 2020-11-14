package pl.edu.pjwstk.jaz.task.second;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.pjwstk.jaz.IntegrationTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@IntegrationTest
public class TestAccessController {
    @Test
    public void shouldReturnCode200WhenRequestingAccessToEndpoint_forAll() {
        given()
                .when()
                .get("/api/forAll")
                .then()
                .statusCode(equalTo((200)))
                .body((equalTo("forAll")));
    }
}