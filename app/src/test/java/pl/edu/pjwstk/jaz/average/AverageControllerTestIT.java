package pl.edu.pjwstk.jaz.average;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.pjwstk.jaz.IntegrationTest;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@IntegrationTest
public class AverageControllerTestIT {
    @Test
    public void should_calculate_simple_average() {
        var response = given()
                .param("numbers", "1,2,3,4")
                .when()
                .get("/api/average")
                .then()
                .statusCode(200)
                .body(equalTo("2.5"));
    }
}