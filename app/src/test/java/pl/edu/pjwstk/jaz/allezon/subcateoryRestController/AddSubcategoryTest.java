package pl.edu.pjwstk.jaz.allezon.subcateoryRestController;

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
public class AddSubcategoryTest {
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
        String json = "{ \"email\": \"userForAddSubcategory@jaz.com\", \"password\": \"password\" }";
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
        String json = "{ \"email\": \"userForAddSubcategory@jaz.com\", \"password\": \"password\" }";
        return given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn()
                .getCookies();
    }

    @Test
    public void shouldReturn201WhenAddNewSubcategoryAsAdmin() {
        String json = "{ \"name\": \"newCategoryForSubcategory\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        json = "{ \"categoryName\": \"newCategoryForSubcategory\", \"subcategoryName\":\"subcategory newCategoryForSubcategory\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        given()
                .get("/api/allezon/categories/newCategoryForSubcategory")
                .then()
                .body(equalTo("[\"subcategory newCategoryForSubcategory\"]"))
                .statusCode(equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void shouldReturn403WhenAddNewCategoryAsStandardUser() {
        String json = "{ \"name\": \"newCategoryForSubcategoryAsStandardUser\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        json = "{ \"categoryName\": \"newCategoryForSubcategoryAsStandardUser\", \"subcategoryName\":\"subcategory newCategoryForSubcategory\" }";
        given()
                .cookies(userCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }

    @Test
    public void shouldReturn403WhenAddNewCategoryAsGuest() {
        String json = "{ \"name\": \"newCategoryForSubcategoryAsGuest\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        json = "{ \"categoryName\": \"newCategoryForSubcategoryAsGuest\", \"subcategoryName\":\"subcategory newCategoryForSubcategory\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
    }

    @Test
    public void shouldReturn409WhenAddRedundantCategoryAsAdmin() {
        String json = "{ \"name\": \"newRedundantCategoryForSubcategory\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        json = "{ \"categoryName\": \"newRedundantCategoryForSubcategory\", \"subcategoryName\":\"subcategory newCategoryForSubcategory\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CONFLICT));
    }
}