package pl.edu.pjwstk.jaz.allezon.registerController;

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
public class RegisterRestControllerTest {
    @Test
    public void shouldReturn201WhenRegisterWithUniqueEmail() {
        String json = "{ \"email\": \"firstUserRegister@jaz.com\", \"password\": \"password\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .body(equalTo("Registered."))
                .statusCode(equalTo(HttpStatus.SC_CREATED));
    }

    @Test
    public void shouldReturn409WhenRegisterWithUsingEmailThatExists() {
        String json = "{ \"email\": \"secondUserRegister@jaz.com\", \"password\": \"password\" }";
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
                .post("/api/allezon/register")
                .then()
                .body(equalTo("Such an email exists in the database."))
                .statusCode(equalTo(HttpStatus.SC_CONFLICT));
    }

    @Test
    public void shouldReturn400WhenGiveEmptyEmail() {
        String json = "{ \"email\": \"\", \"password\": \"password\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
                .then()
                .body(equalTo("Email or password is empty."))
                .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void shouldReturn400WhenGiveEmptyPassword() {
        String json = "{ \"email\": \"EmptyPasswod@jaz.com\", \"password\": \"\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/register")
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
                .post("/api/allezon/register")
                .then()
                .body(equalTo("Email or password is empty."))
                .statusCode(equalTo(HttpStatus.SC_BAD_REQUEST));
    }
}
