package pl.edu.pjwstk.jaz.allezon.controller;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
public class TestSubcategoryController {
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
    public void shouldReturnCode200AndEmptyListWithCategoryAndSubcategory() {
        given()
                .when()
                .get("/api/allezon/categories/car")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body(equalTo("[]"));
    }

    @Test
    public void shouldReturnCode204WhenGiveSubcategoryWhichDoesNotExists() {
        given()
                .when()
                .get("/api/allezon/categories/subcategoryDoesNotExist")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NO_CONTENT));
    }

    @Test
    public void shouldReturnCode201WhenGiveNewSubcategoryAsAdmin() throws JSONException {
        String json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);

        var response = given()
                .when()
                .get("/api/allezon/categories")
                .thenReturn();

        JSONArray content =  new JSONArray(response.getBody().asString());

        assert (content.getJSONObject(0).getString("name").equals("car") == true);
        assert (response.statusCode() == HttpStatus.SC_OK);

        int categoryId = (int) content.getJSONObject(0).get("id");
        json = "{\"categoryId\":" + categoryId + ", \"name\": \"wheels\"}";

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
    }

    @Test
    public void shouldReturn403CodeWhenTryAddOrDeleteSubcategoryAsUser() throws JSONException {
        String json = "{ \"email\": \"subcategoryUser@test.com\", \"password\": \"kot\"}";
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

        var response = given()
                .when()
                .get("/api/allezon/categories/subcategories")
                .thenReturn();
        //assert (response.statusCode() == HttpStatus.SC_UNAUTHORIZED);
        json = "{\"categoryId\":" + 1 + ", \"name\": \"wheels\"}";

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }

    @Test
    public void shouldReturnCode204WhenDeleteSubcategoryFromCarCategory() throws JSONException {
        String json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
        var cookies = given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn();
        assert (cookies.statusCode() == HttpStatus.SC_OK);

        var response = given()
                .when()
                .get("/api/allezon/categories")
                .thenReturn();

        JSONArray content =  new JSONArray(response.getBody().asString());

        assert (content.getJSONObject(0).getString("name").equals("car") == true);
        assert (response.statusCode() == HttpStatus.SC_OK);

        int categoryId = (int) content.getJSONObject(0).get("id");
        json = "{\"categoryId\":" + categoryId + ", \"name\": \"wheels\"}";

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NO_CONTENT));

        given()
                .when()
                .get("/api/allezon/categories/car")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body(equalTo("[]"));
    }
}
