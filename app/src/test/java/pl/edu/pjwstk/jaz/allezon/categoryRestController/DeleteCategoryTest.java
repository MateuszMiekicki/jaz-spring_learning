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
public class DeleteCategoryTest {
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
        String json = "{ \"email\": \"userForDeleteCategory@jaz.com\", \"password\": \"password\" }";
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
    public void shouldReturn204WhenPassCategoryToDeleteAsAdmin(){
        String json = "{ \"name\": \"categoryForDelete\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NO_CONTENT));
    }

    @Test
    public void shouldReturn400WhenPassEmptyCategoryNameToDeleteAsAdmin(){
        String json = "{ \"name\": \"categoryForDelete\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        json = "{ \"name\": \"\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void shouldReturn404WhenPassCategoryWhichDoesNotExistsToDeleteAsAdmin(){
        String json = "{ \"name\": \"categoryForDeleteDoesNotExists\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    public void shouldReturn403WhenTryDeleteCategoryAsStandardUser(){
        String json = "{ \"name\": \"categoryForDeleteByStandardUser\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }
}
