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
public class EditSubcategoryTest {
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
        String json = "{ \"email\": \"userForEditSubcategory@jaz.com\", \"password\": \"password\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .body(equalTo("Registered."))
                .statusCode(equalTo(HttpStatus.SC_CREATED));

        json = "{ \"name\": \"newCategoryForSubcategoryEditVersion\" }";
        given()
                .cookies(
                        given()
                                .contentType(ContentType.JSON)
                                .body("{ \"email\": \"admin@jaz.com\", \"password\": \"admin\" }")
                                .when()
                                .post("/api/allezon/login")
                                .thenReturn()
                                .getCookies()
                )
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));

        json = "{ \"name\": \"second newCategoryForSubcategoryEditVersion\" }";
        given()
                .cookies(
                        given()
                                .contentType(ContentType.JSON)
                                .body("{ \"email\": \"admin@jaz.com\", \"password\": \"admin\" }")
                                .when()
                                .post("/api/allezon/login")
                                .thenReturn()
                                .getCookies()
                )
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
    }

    public Map<String, String> userCookies() {
        String json = "{ \"email\": \"userForEditSubcategory@jaz.com\", \"password\": \"password\" }";
        return given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .thenReturn()
                .getCookies();
    }

    @Test
    public void shouldReturn204WhenPassCategoryToEditAsAdmin() {
        String json = "{ \"categoryName\": \"newCategoryForSubcategoryEditVersion\", \"subcategoryName\":\"subcategory newCategoryForSubcategory\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        given()
                .get("/api/allezon/categories/newCategoryForSubcategoryEditVersion")
                .then()
                .body(equalTo("[\"subcategory newCategoryForSubcategory\"]"))
                .statusCode(equalTo(HttpStatus.SC_OK));
        json = "{ \"categoryName\": \"newCategoryForSubcategoryEditVersion\", \"subcategoryName\":\"subcategory newCategoryForSubcategory\", \"newSubcategoryName\":\"new name\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NO_CONTENT));
        given()
                .get("/api/allezon/categories/newCategoryForSubcategoryEditVersion")
                .then()
                .body(equalTo("[\"new name\"]"))
                .statusCode(equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void shouldReturn404WhenPassCategoryToEditAsAdmin() {
        String json = "{ \"categoryName\": \"second newCategoryForSubcategoryEditVersion\", \"subcategoryName\":\"subcategory newCategoryForSubcategory\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        given()
                .get("/api/allezon/categories/newCategoryForSubcategoryEditVersion")
                .then()
                .body(equalTo("[\"new name\"]"))
                .statusCode(equalTo(HttpStatus.SC_OK));
        json = "{ \"categoryName\": \"second newCategoryForSubcategoryEditVersion\", \"subcategoryName\":\" newCategoryForSubcategory\", \"newSubcategoryName\":\"new name\" }";
        given()
                .cookies(administratorCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .put("/api/allezon/categories/subcategories")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NOT_FOUND));
        given()
                .get("/api/allezon/categories/newCategoryForSubcategoryEditVersion")
                .then()
                .body(equalTo("[\"new name\"]"))
                .statusCode(equalTo(HttpStatus.SC_OK));
    }
}
