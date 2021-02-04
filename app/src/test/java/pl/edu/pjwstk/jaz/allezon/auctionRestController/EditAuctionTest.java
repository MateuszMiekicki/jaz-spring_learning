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
public class EditAuctionTest {
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
        String json = "{ \"email\": \"editAuctionTest@jaz.com\", \"password\": \"password\" }";
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
                .body("{ \"name\": \"editAuctionCategory\" }")
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
                .body("{ \"categoryName\": \"editAuctionCategory\", \"subcategoryName\":\"deleteAuctionSubcategory\" }")
                .when()
                .post("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
    }

    public Map<String, String> userCookies() {
        String json = "{ \"email\": \"editAuctionTest@jaz.com\", \"password\": \"password\" }";
        return given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn()
                .getCookies();
    }

    @Test
    public void shouldReturn200WhenEditOwnAuction(){
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body("{ \"categoryName\": \"editAuctionCategory\", \"subcategoryName\": \"deleteAuctionSubcategory\", \"title\": \"3\", \"description\": \"4\", \"price\": 54.23, \"auctionImages\": [ { \"url\": \"adasdas\" }, { \"url\": \"asdf\" }, { \"url\": \"asf\" }, { \"url\": \"zvxcz vac\" } ], \"auctionParameters\": [ { \"name\": \"parm1\", \"value\": \"adas\" }, { \"name\": \"parm1\", \"value\": \"adas\" }, { \"name\": \"parm1\", \"value\": \"adas\" } ] }")
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        JSONArray responseBody = new JSONArray(given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/allezon/auctions/editAuctionTest@jaz.com")
                .thenReturn()
                .getBody()
                .asString());
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body("{\"auctionId\":" + responseBody.get(0) + ",\"price\": 54.23, \"auctionImages\": [ ], \"auctionParameters\": []}")
                .when()
                .put("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void shouldReturn401WhenEditOwnAuction(){
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body("{ \"categoryName\": \"editAuctionCategory\", \"subcategoryName\": \"deleteAuctionSubcategory\", \"title\": \"3\", \"description\": \"4\", \"price\": 54.23, \"auctionImages\": [ { \"url\": \"adasdas\" }, { \"url\": \"asdf\" }, { \"url\": \"asf\" }, { \"url\": \"zvxcz vac\" } ], \"auctionParameters\": [ { \"name\": \"parm1\", \"value\": \"adas\" }, { \"name\": \"parm1\", \"value\": \"adas\" }, { \"name\": \"parm1\", \"value\": \"adas\" } ] }")
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        JSONArray responseBody = new JSONArray(given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/allezon/auctions/admin@jaz.com")
                .thenReturn()
                .getBody()
                .asString());
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body("{\"auctionId\":" + responseBody.get(0) + ",\"price\": 54.23, \"auctionImages\": [ ], \"auctionParameters\": []}")
                .when()
                .put("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_UNAUTHORIZED));
    }
}
