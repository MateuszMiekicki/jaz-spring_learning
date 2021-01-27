package pl.edu.pjwstk.jaz.allezon.controller;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.pjwstk.jaz.IntegrationTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@IntegrationTest
public class TestAuctionController {
    @BeforeEach
    public void initialize() {
        given()
                .when()
                .get("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body(equalTo("[]"));

        String json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);

        json = "{ \"name\": \"car\"}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
    }

    @AfterEach
    public void cleanUp() {
        String json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);

        json = "{ \"name\": \"car\"}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NO_CONTENT));
    }

    @Test
    public void shouldReturn403WhenTryAddAuctionAsNotLoggedIn() {
        String json = "{ \"categoryId\": \"5\", \"subcategoryId\": \"7\", \"title\": \"title\", \"description\": \"description\", \"price\": 12.4, \"images\": [ { \"url\": \"ala\" }, { \"url\": \"ala\" } ] }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }

    @Test
    public void shouldReturn403WhenTryDeleteAuctionAsNotLoggedIn() {
        String json = "{ \"categoryId\": \"5\", \"subcategoryId\": \"7\", \"title\": \"title\", \"description\": \"description\", \"price\": 12.4, \"images\": [ { \"url\": \"ala\" }, { \"url\": \"ala\" } ] }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }

    @Test
    public void shouldReturn403WhenTryModifyAuctionAsNotLoggedIn() {
        String json = "{ \"categoryId\": \"5\", \"subcategoryId\": \"7\", \"title\": \"title\", \"description\": \"description\", \"price\": 12.4, \"images\": [ { \"url\": \"ala\" }, { \"url\": \"ala\" } ] }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }

    @Test
    public void shouldReturn201WhenTryAddAuctionAsUser() {
        String json = "{ \"categoryName\": \"car\", \"subcategoryName\": \"damper\", \"title\": \"damper car\", \"description\": \"damper for audi\", \"price\": 12.4, \"images\": [ { \"url\": \"front\" }, { \"url\": \"back\" } ] }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }
}
