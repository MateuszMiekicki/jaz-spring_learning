package pl.edu.pjwstk.jaz.allezon.categoryRestController;

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
public class EditCategoryTest {
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
        String json = "{ \"email\": \"userForEditCategory@jaz.com\", \"password\": \"password\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .body(equalTo("Registered."))
                .statusCode(equalTo(HttpStatus.SC_CREATED));
    }

    public Map<String, String> userCookies() {
        String json = "{ \"email\": \"userForEditCategory@jaz.com\", \"password\": \"password\" }";
        return given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn()
                .getCookies();
    }

    @Test
    public void shouldReturn200WhenPassCategoryToEditAsAdmin(){
        String json = "{ \"name\": \"categoryForEditFirst\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        json = "{ \"name\": \"categoryForEditFirst\", \"newName\":\"categoryAfterEdit\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void shouldReturn400WhenPassCategoryToEditWithEmptyNameAsAdmin(){
        String json = "{ \"name\": \"categoryForEditSecond\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        json = "{ \"name\": \"\", \"newName\":\"categoryAfterEdit\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void shouldReturn400WhenPassCategoryToEditWithEmptyNewNameAsAdmin(){
        String json = "{ \"name\": \"categoryForEditThird\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        json = "{ \"name\": \"categoryForEdit\", \"newName\":\"\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void shouldReturn404WhenPassCategoryWithDoesNotExistsAsAdmin(){
        String json = "{ \"name\": \"categoryForEditFourth\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        json = "{ \"name\": \"thisCategoryDoesExistsForEdit\", \"newName\":\"newNameForCategory\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    public void shouldReturn403WhenPassCategoryWithDoesNotExistsAsStandardUser(){
        String json = "{ \"name\": \"categoryForEditFifth\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        json = "{ \"name\": \"thisCategoryDoesExistsForEdit\", \"newName\":\"newNameForCategory\" }";
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }
}
