package pl.edu.pjwstk.jaz.allezon.auctionRestController;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.pjwstk.jaz.IntegrationTest;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@IntegrationTest
public class DeleteAuctionTest {
    public Map<String, String> administratorCookies() {
        String json = "{ \"email\": \"admin@jaz.com\", \"password\": \"admin\" }";
        return given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn()
                .getCookies();
    }

    @BeforeAll
    public static void registerUser() {
        String json = "{ \"email\": \"deleteAuctionTest@jaz.com\", \"password\": \"password\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .body(equalTo("Registered."))
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        given()
                .cookies(given()
                        .contentType(ContentType.JSON)
                        .body("{ \"email\": \"admin@jaz.com\", \"password\": \"admin\" }")
                        .when()
                        .post("/api/allezon/login")
                        .thenReturn()
                        .getCookies())
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"deleteAuctionCategory\" }")
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        given()
                .cookies(given()
                        .contentType(ContentType.JSON)
                        .body("{ \"email\": \"admin@jaz.com\", \"password\": \"admin\" }")
                        .when()
                        .post("/api/allezon/login")
                        .thenReturn()
                        .getCookies())
                .contentType(ContentType.JSON)
                .body("{ \"categoryName\": \"deleteAuctionCategory\", \"subcategoryName\":\"deleteAuctionSubcategory\" }")
                .when()
                .post("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
    }

    public Map<String, String> userCookies() {
        String json = "{ \"email\": \"deleteAuctionTest@jaz.com\", \"password\": \"password\" }";
        return given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn()
                .getCookies();
    }

    @Test
    public void shouldReturn204WhenDeleteOwnAuction() {
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body("{ \"categoryName\": \"deleteAuctionCategory\", \"subcategoryName\": \"deleteAuctionSubcategory\", \"title\": \"3\", \"description\": \"4\", \"price\": 54.23, \"auctionImages\": [ { \"url\": \"adasdas\" }, { \"url\": \"asdf\" }, { \"url\": \"asf\" }, { \"url\": \"zvxcz vac\" } ], \"auctionParameters\": [ { \"name\": \"parm1\", \"value\": \"adas\" }, { \"name\": \"parm1\", \"value\": \"adas\" }, { \"name\": \"parm1\", \"value\": \"adas\" } ] }")
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        JSONArray responseBody = new JSONArray(given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/allezon/auctions/deleteAuctionTest@jaz.com")
                .thenReturn()
                .getBody()
                .asString());
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body("{\"auctionId\":" + responseBody.get(0) + "}")
                .when()
                .delete("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NO_CONTENT));
    }

    @Test
    public void shouldReturn204WhenDeleteAuctionUserAsAdmin() {
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body("{ \"categoryName\": \"deleteAuctionCategory\", \"subcategoryName\": \"deleteAuctionSubcategory\", \"title\": \"3\", \"description\": \"4\", \"price\": 54.23, \"auctionImages\": [ { \"url\": \"adasdas\" }, { \"url\": \"asdf\" }, { \"url\": \"asf\" }, { \"url\": \"zvxcz vac\" } ], \"auctionParameters\": [ { \"name\": \"parm1\", \"value\": \"adas\" }, { \"name\": \"parm1\", \"value\": \"adas\" }, { \"name\": \"parm1\", \"value\": \"adas\" } ] }")
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        JSONArray responseBody = new JSONArray(given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/allezon/auctions/deleteAuctionTest@jaz.com")
                .thenReturn()
                .getBody()
                .asString());
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body("{\"auctionId\":" + responseBody.get(0) + "}")
                .when()
                .delete("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NO_CONTENT));
    }

    @Test
    public void shouldReturn403WhenDeleteAuctionUserAsGuest() {
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body("{ \"categoryName\": \"deleteAuctionCategory\", \"subcategoryName\": \"deleteAuctionSubcategory\", \"title\": \"3\", \"description\": \"4\", \"price\": 54.23, \"auctionImages\": [ { \"url\": \"adasdas\" }, { \"url\": \"asdf\" }, { \"url\": \"asf\" }, { \"url\": \"zvxcz vac\" } ], \"auctionParameters\": [ { \"name\": \"parm1\", \"value\": \"adas\" }, { \"name\": \"parm1\", \"value\": \"adas\" }, { \"name\": \"parm1\", \"value\": \"adas\" } ] }")
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        JSONArray responseBody = new JSONArray(given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/allezon/auctions/deleteAuctionTest@jaz.com")
                .thenReturn()
                .getBody()
                .asString());
        given()
                .contentType(ContentType.JSON)
                .body("{\"auctionId\":" + responseBody.get(0) + "}")
                .when()
                .delete("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }
}
