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
                .body(equalTo("Average equals: 2.5"));
    }

    @Test
    public void should_calculate_simple_integer_average() {
        var response = given()
                .param("numbers", "4,3,1,7,5")
                .when()
                .get("/api/average")
                .then()
                .statusCode(200)
                .body(equalTo("Average equals: 4"));
    }

    @Test
    public void should_calculate_simple_measurable_average() {
        var response = given()
                .param("numbers", "2,1")
                .when()
                .get("/api/average")
                .then()
                .statusCode(200)
                .body(equalTo("Average equals: 1.5"));
    }

    @Test
    public void should_calculate_simple_periodic_average() {
        var response = given()
                .param("numbers", "2,1,1")
                .when()
                .get("/api/average")
                .then()
                .statusCode(200)
                .body(equalTo("Average equals: 1.33"));
    }

    @Test
    public void should_inform_about_lack_of_parameters() {
        var response = given()
                .param("numbers", "")
                .when()
                .get("/api/average")
                .then()
                .statusCode(200)
                .body(equalTo("Please put parameters."));
    }
}