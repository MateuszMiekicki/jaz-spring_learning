package pl.edu.pjwstk.jaz.task.second;

import io.restassured.http.ContentType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.pjwstk.jaz.IntegrationTest;
import pl.edu.pjwstk.jaz.task.second.component.AccountController;
import pl.edu.pjwstk.jaz.task.second.component.UserDTO;
import pl.edu.pjwstk.jaz.task.second.exception.BadCredentialsException;
import pl.edu.pjwstk.jaz.task.second.exception.UserExistException;
import pl.edu.pjwstk.jaz.task.second.exception.UserNotFoundException;
import pl.edu.pjwstk.jaz.task.second.repository.UsersRepository;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@IntegrationTest
public class TestAccountController {
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

    @Test
    public void shouldReturnCode200AndUserDetailsWhenPassUsername() {
        UserDTO userAla = new UserDTO();
        userAla.setUsername("ala");
        userAla.setPassword("kot");
        userAla.setRole("");
        when(usersRepository.getUser("ala")).thenReturn(userAla);

        UserDTO userBartek = new UserDTO();
        userBartek.setUsername("ala");
        userBartek.setPassword("kot");
        userBartek.setRole("");
        when(usersRepository.getUser("bartek")).thenReturn(userBartek);

        given()
                .param("username", "ala,bartek")
                .when()
                .get("/api/user")
                .then()
                .statusCode(200)
                .body(equalTo(userAla.toString() + ",\n" + userBartek.toString()));
    }

    @Test
    public void shouldReturnCode204AndShortMessageWhenTryGetInformationAboutUserDoesExist() {
        doThrow(new UserNotFoundException("There is no user with this name")).when(usersRepository).getUser("ala");
        given()
                .param("username", "ala")
                .when()
                .get("/api/user")
                .then()
                .statusCode(204)
                .body(equalTo(""));
    }

    @Test
    public void shouldReturnCode20AndShortMessageWhenNotPassAnything() {
        given()
                .param("username", "")
                .when()
                .get("/api/user")
                .then()
                .statusCode(200)
                .body(equalTo("You must provide a user name."));
    }
}
