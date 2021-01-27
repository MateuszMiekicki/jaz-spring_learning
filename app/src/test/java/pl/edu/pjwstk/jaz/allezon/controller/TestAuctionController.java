package pl.edu.pjwstk.jaz.allezon.controller;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.*;
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

        var response = given()
                .when()
                .get("/api/allezon/categories")
                .thenReturn();
        try {
            JSONArray content = new JSONArray(response.getBody().asString());
            assert (content.getJSONObject(0).getString("name").equals("car") == true);
            assert (response.statusCode() == HttpStatus.SC_OK);

            int categoryId = (int) content.getJSONObject(0).get("id");
            json = "{\"categoryId\":" + categoryId + ", \"name\": \"damper\"}";

            given()
                    .cookies(cookies.getCookies())
                    .contentType(ContentType.JSON)
                    .body(json)
                    .when()
                    .post("/api/allezon/categories/subcategories")
                    .then()
                    .statusCode(equalTo(HttpStatus.SC_CREATED));
        } catch (JSONException e) {
            e.printStackTrace();
            assert (false);
        }
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
        String json = "{ \"categoryName\": \"car\", \"subcategoryName\": \"damper\", \"title\": \"title\", \"description\": \"description\", \"price\": 12.4, \"images\": [ { \"url\": \"ala\" }, { \"url\": \"ala\" } ], \"parameters\": [ { \"name\": \"parm1\", \"value\": \"off\" } ] }";
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
        String json = "{ \"categoryName\": \"car\", \"subcategoryName\": \"damper\", \"title\": \"title\", \"description\": \"description\", \"price\": 12.4, \"images\": [ { \"url\": \"ala\" }, { \"url\": \"ala\" } ], \"parameters\": [ { \"name\": \"parm1\", \"value\": \"off\" } ] }";
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
        String json = "{ \"categoryName\": \"car\", \"subcategoryName\": \"damper\", \"title\": \"title\", \"description\": \"description\", \"price\": 12.4, \"images\": [ { \"url\": \"ala\" }, { \"url\": \"ala\" } ], \"parameters\": [ { \"name\": \"parm1\", \"value\": \"off\" } ] }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }

    @Test
    public void shouldReturn201WhenTryAddAndDeleteAuctionAsUser() throws JSONException {
        String json = "{ \"email\": \"aucttionDamperUser@test.com\", \"password\": \"kot\"}";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED))
                .body(equalTo("Registered."));
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);

        json = "{ \"categoryName\": \"car\", \"subcategoryName\": \"damper\", \"title\": \"title\", \"description\": \"description\", \"price\": 12.4, \"images\": [ { \"url\": \"ala\" }, { \"url\": \"ala\" } ], \"parameters\": [ { \"name\": \"parm1\", \"value\": \"off\" } ] }";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));

        var response = given()
                .when()
                .get("/api/allezon/auctions")
                .thenReturn();

        JSONArray content = new JSONArray(response.getBody().asString());

        assert (response.statusCode() == HttpStatus.SC_OK);

        String auctionId = content.getJSONObject(0).getString("id");
        json = "{ \"auctionId\": \"" + auctionId + "\"}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NO_CONTENT));
    }

    @Test
    public void shouldReturn404WhenTryDeleteAuctionForAnotherUser() throws JSONException {
        String json = "{ \"email\": \"aucttionDamperUserOne@test.com\", \"password\": \"kot\"}";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED))
                .body(equalTo("Registered."));
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);

        json = "{ \"categoryName\": \"car\", \"subcategoryName\": \"damper\", \"title\": \"title\", \"description\": \"description\", \"price\": 12.4, \"images\": [ { \"url\": \"ala\" }, { \"url\": \"ala\" } ], \"parameters\": [ { \"name\": \"parm1\", \"value\": \"off\" } ] }";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));

        json = "{ \"email\": \"aucttionDamperUserTwo@test.com\", \"password\": \"kot\"}";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED))
                .body(equalTo("Registered."));
        cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);

        var response = given()
                .when()
                .get("/api/allezon/auctions")
                .thenReturn();

        JSONArray content = new JSONArray(response.getBody().asString());

        assert (response.statusCode() == HttpStatus.SC_OK);

        String auctionId = content.getJSONObject(0).getString("id");
        json = "{ \"auctionId\": \"" + auctionId + "\"}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_FOUND));

        json = "{ \"email\": \"aucttionDamperUserOne@test.com\", \"password\": \"kot\"}";
        cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);
        auctionId = content.getJSONObject(0).getString("id");
        json = "{ \"auctionId\": \"" + auctionId + "\"}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/auctions")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NO_CONTENT));
    }
}
