package pl.edu.pjwstk.jaz.allezon.loginController;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.pjwstk.jaz.IntegrationTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@IntegrationTest
public class LoginRestControllerTest {
    @Test
    public void shouldReturn200WhenLoginWithCorrectData() {
        String json = "{ \"email\": \"firstUserLogin@jaz.com\", \"password\": \"password\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .body(equalTo("Registered."))
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .then()
                .body(equalTo("Logged in."))
                .statusCode(equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void shouldReturn200WhenLoginAsAdmin() {
        String json = "{ \"email\": \"admin@jaz.com\", \"password\": \"admin\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .then()
                .body(equalTo("Logged in."))
                .statusCode(equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void shouldReturn400WhenGiveEmptyPassword() {
        String json = "{ \"email\": \"EmptyPasswod@jaz.com\", \"password\": \"\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .then()
                .body(equalTo("Email or password is empty."))
                .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void shouldReturn400WhenGiveEmptyEmailAndPassword() {
        String json = "{ \"email\": \"\", \"password\": \"\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .then()
                .body(equalTo("Email or password is empty."))
                .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void shouldReturn201WhenLoginWithWrongPassword() {
        String json = "{ \"email\": \"secondUserLogin@jaz.com\", \"password\": \"password\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .body(equalTo("Registered."))
                .statusCode(equalTo(HttpStatus.SC_CREATED));
        json = "{ \"email\": \"secondUserLogin@jaz.com\", \"password\": \"wrongPassword\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .then()
                .body(equalTo("Email or password is incorrect."))
                .statusCode(equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    public void shouldReturn400WhenLoginWithEmailWhichDoesNotExist() {
        String json = "{ \"email\": \"thisemaildefinitelydoesnotexist@jaz.com\", \"password\": \"password\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/login")
                .then()
                .body(equalTo("Such an email not exists in the database."))
                .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST));
    }
}
