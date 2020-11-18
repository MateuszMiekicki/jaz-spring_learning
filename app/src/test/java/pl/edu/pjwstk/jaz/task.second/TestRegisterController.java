package pl.edu.pjwstk.jaz.task.second;

import io.restassured.http.ContentType;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.pjwstk.jaz.IntegrationTest;
import pl.edu.pjwstk.jaz.task.second.component.UserDTO;
import pl.edu.pjwstk.jaz.task.second.component.restController.AccountController;
import pl.edu.pjwstk.jaz.task.second.exception.BadCredentialsException;
import pl.edu.pjwstk.jaz.task.second.exception.UserExistException;
import pl.edu.pjwstk.jaz.task.second.repository.UsersRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@RunWith(SpringRunner.class)
@IntegrationTest
public class TestRegisterController {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @MockBean
    UsersRepository usersRepository;
    @InjectMocks
    AccountController controller;

    @BeforeEach
    public void setup() {
        controller = new AccountController(usersRepository);
    }

    @Test
    public void shouldReturnCode201WhenPassingUserDTOInJSONFormat() {
        String json = "{ \"username\": \"ala\", \"password\": \"kot\", \"role\": \"admin\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/register")
                .then()
                .statusCode(equalTo(201))
                .body(equalTo("Created"));
    }

    @Test
    public void shouldReturnCode200AndShortMessageWhenPassingUserWhichExists() {
        doThrow(new UserExistException("A user with this name already exists: ala")).when(usersRepository).insert(any(UserDTO.class));
        String json = "{ \"username\": \"ala\", \"password\": \"kot\", \"role\": \"admin\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/register")
                .then()
                .statusCode(equalTo(200))
                .body(equalTo("A user with this name already exists: ala"));
    }

    @Test
    public void shouldReturnCode400AndShortMessageWhenPassingUserWhichEmptyUsername() {
        doThrow(new BadCredentialsException("Incorrect data. Password or login is empty.")).when(usersRepository).insert(any(UserDTO.class));
        String json = "{ \"username\": \"\", \"password\": \"kot\", \"role\": \"admin\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/register")
                .then()
                .statusCode(equalTo(200))
                .body(equalTo("Incorrect data. Password or login is empty."));
    }

    @Test
    public void shouldReturnCode400AndShortMessageWhenPassingUserWhichEmptyPassword() {
        doThrow(new BadCredentialsException("Incorrect data. Password or login is empty.")).when(usersRepository).insert(any(UserDTO.class));
        String json = "{ \"username\": \"ala\", \"password\": \"\", \"role\": \"admin\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/register")
                .then()
                .statusCode(equalTo(200))
                .body(equalTo("Incorrect data. Password or login is empty."));
    }

    @Test
    public void shouldReturnCode400AndShortMessageWhenPassingUserWhichEmptyRole() {
        String json = "{ \"username\": \"ala\", \"password\": \"\", \"role\": \"\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/register")
                .then()
                .statusCode(equalTo(201))
                .body(equalTo("Created"));
    }
}
