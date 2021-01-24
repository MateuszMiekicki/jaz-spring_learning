package pl.edu.pjwstk.jaz.allezon.controller;

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
public class TestSectionController {
    @Test
    public void completeTestSectionForAdminRole() {
        given()
                .when()
                .get("/api/allezon/sections")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body(equalTo("[]"));
        String json = "{ \"name\": \"car\"}";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/sections/add")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/sections/add")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));

        json = "{ \"email\": \"admin\", \"password\": \"admin\"}";
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
                .post("/api/allezon/sections/add")
                .then()
                .statusCode(equalTo(HttpStatus.SC_CREATED));

        given()
                .when()
                .get("/api/allezon/sections")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body(equalTo("[{\"id\":2,\"name\":\"car\"}]"));

        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/sections/delete")
                .then()
                .statusCode(equalTo(HttpStatus.SC_NO_CONTENT));

        given()
                .when()
                .get("/api/allezon/sections")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body(equalTo("[]"));
    }

    @Test
    public void shouldReturnForbiddenCodeWhenAuthenticationUserTryGetResourceDesignedForAdmin() {
        String json = "{ \"email\": \"sectionsUser@test.com\", \"password\": \"kot\"}";
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

        json = "{ \"name\": \"car\"}";
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allezon/sections/add")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
        given()
                .cookies(cookies.getCookies())
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .delete("/api/allezon/sections/delete")
                .then()
                .statusCode(equalTo(HttpStatus.SC_FORBIDDEN));
        given()
                .cookies(cookies.getCookies())
                .when()
                .get("/api/allezon/sections")
                .then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body(equalTo("[]"));
    }
}
