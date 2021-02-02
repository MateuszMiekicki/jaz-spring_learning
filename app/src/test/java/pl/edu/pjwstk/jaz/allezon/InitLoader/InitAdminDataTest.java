package pl.edu.pjwstk.jaz.allezon.InitLoader;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.pjwstk.jaz.IntegrationTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@IntegrationTest
public class InitAdminDataTest {
    @Test
    public void shouldReturn201WhenRegisterWithUniqueEmail() {
        String json = "{ \"email\": \"admin@jaz.com\", \"password\": \"admin\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .body(equalTo("Such an email exists in the database."))
                .statusCode(equalTo(HttpStatus.SC_CONFLICT));
    }
}
