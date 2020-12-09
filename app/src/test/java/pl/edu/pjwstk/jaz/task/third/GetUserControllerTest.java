package pl.edu.pjwstk.jaz.task.third;

import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.pjwstk.jaz.IntegrationTest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import java.net.HttpCookie;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@IntegrationTest
public class GetUserControllerTest {
    @Test
    @Order(2)
    public void shouldReturnCode401WhenTryGetInformationAsUnauthorizedUser() {
        given()
                .get("/api/third/getUser/ala")
                .then()
                .statusCode(equalTo(401));
    }

    @Test
    @Order(3)
    public void shouldReturnCode204WhenPassUserWhichDoesMotExist() {
        String json = "{ \"username\": \"pawel\", \"password\": \"kot\", \"idRole\": 2 }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/third/register")
                .then()
                .statusCode(equalTo(201))
                .body(equalTo("Registered."));
        json = "{ \"username\": \"pawel\", \"password\": \"kot\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/third/login")
                .thenReturn();
        given()
                .cookies(cookies.getCookies())
                .get("/api/third/getUser/pawels")
                .then()
                .statusCode(equalTo(204));
    }

    @Test
    @Order(1)
    public void shouldReturnCode200AmdContentWithBaseInformationWhenUsername() {
        String json = "{ \"username\": \"ala\", \"password\": \"kot\", \"idRole\": 2 }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/third/register")
                .then()
                .statusCode(equalTo(201))
                .body(equalTo("Registered."));
        json = "{ \"username\": \"ala\", \"password\": \"kot\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/third/login")
                .thenReturn();
        given()
                .cookies(cookies.getCookies())
                .get("/api/third/getUser/ala")
                .then()
                .statusCode(equalTo(200))
                .body(equalTo("UserEntity{id=2, username='ala', idRole=2}"));
    }
}
