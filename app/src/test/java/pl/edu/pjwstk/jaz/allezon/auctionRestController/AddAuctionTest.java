package pl.edu.pjwstk.jaz.allezon.auctionRestController;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
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
public class AddAuctionTest {
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
        String json = "{ \"email\": \"addAuctionTest@jaz.com\", \"password\": \"password\" }";
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
                .body("{ \"name\": \"addAuctionCategory\" }")
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
                .body("{ \"categoryName\": \"addAuctionCategory\", \"subcategoryName\":\"addAuctionSubcategory\" }")
                .when()
                .post("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
    }

    public Map<String, String> userCookies() {
        String json = "{ \"email\": \"addAuctionTest@jaz.com\", \"password\": \"password\" }";
        return given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn()
                .getCookies();
    }

    @Test
    public void shouldReturn201WhenCreateCorrectAuctionAsUser() {
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body("{\"categoryName\": \"addAuctionCategory\", \"subcategoryName\": \"addAuctionSubcategory\", \"title\": \"3\", \"description\": \"4\", \"price\": 54.23, \"auctionImages\": [ ], \"auctionParameters\": [ ]}")
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED))
                .body(equalTo("Added auction."));
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body("{\"categoryName\": \"addAuctionCategory\", \"subcategoryName\": \"addAuctionSubcategory\", \"title\": \"3\", \"description\": \"4\", \"price\": 54.23, \"auctionImages\": [ ], \"auctionParameters\": [ ]}")
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED))
                .body(equalTo("Added auction."));

    }

    @Test
    public void shouldReturn201WhenCreateCorrectAuctionAsAdmin() {
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body("{ \"categoryName\": \"addAuctionCategory\", \"subcategoryName\": \"addAuctionSubcategory\", \"title\": \"3\", \"description\": \"4\", \"price\": 54.23, \"auctionImages\": [ { \"url\": \"adasdas\" }, { \"url\": \"asdf\" }, { \"url\": \"asf\" }, { \"url\": \"zvxcz vac\" } ], \"auctionParameters\": [ { \"name\": \"parm1\", \"value\": \"adas\" }, { \"name\": \"parm1\", \"value\": \"adas\" }, { \"name\": \"parm1\", \"value\": \"adas\" } ] }")
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
    }

    @Test
    public void shouldReturn201WhenCreateCorrectAuctionAsGuest() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"categoryName\": \"addAuctionCategory\", \"subcategoryName\": \"addAuctionSubcategory\", \"title\": \"3\", \"description\": \"4\", \"price\": 54.23, \"auctionImages\": [ ], \"auctionParameters\": [ ]}")
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }

    @Test
    public void shouldReturn406WhenCreateAuctionWithoutARequestedParameter() {
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body("{\"categoryName\": \"\", \"subcategoryName\": \"addAuctionSubcategory\", \"title\": \"3\", \"description\": \"4\", \"price\": 54.23, \"auctionImages\": [ ], \"auctionParameters\": [ ]}")
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_ACCEPTABLE))
                .body(equalTo("Incomplete data."));
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body("{\"categoryName\": \"addAuctionCategory\", \"subcategoryName\": \"\", \"title\": \"3\", \"description\": \"4\", \"price\": 54.23, \"auctionImages\": [ ], \"auctionParameters\": [ ]}")
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_ACCEPTABLE))
                .body(equalTo("Incomplete data."));
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body("{\"categoryName\": \"addAuctionCategory\", \"subcategoryName\": \"addAuctionSubcategory\", \"title\": \"\", \"description\": \"4\", \"price\": 54.23, \"auctionImages\": [ ], \"auctionParameters\": [ ]}")
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_ACCEPTABLE))
                .body(equalTo("Incomplete data."));
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body("{\"categoryName\": \"addAuctionCategory\", \"subcategoryName\": \"addAuctionSubcategory\", \"title\": \"3\", \"description\": \"\", \"price\": 54.23, \"auctionImages\": [ ], \"auctionParameters\": [ ]}")
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_ACCEPTABLE))
                .body(equalTo("Incomplete data."));
    }

    @Test
    public void shouldReturn404WhenCreateCorrectAuctionWithCategoryDoesExist() {
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body("{\"categoryName\": \"asdfas212df\", \"subcategoryName\": \"addAuctionSubcategory\", \"title\": \"3\", \"description\": \"\", \"price\": 54.23, \"auctionImages\": [ ], \"auctionParameters\": [ ]}")
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_ACCEPTABLE))
                .body(equalTo("Incomplete data."));
    }
}
