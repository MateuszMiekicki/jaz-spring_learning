package pl.edu.pjwstk.jaz.task.second;

import io.restassured.http.ContentType;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.pjwstk.jaz.IntegrationTest;
import pl.edu.pjwstk.jaz.task.second.component.UserDTO;
import pl.edu.pjwstk.jaz.task.second.component.UserSession;
import pl.edu.pjwstk.jaz.task.second.component.restController.LoginController;
import pl.edu.pjwstk.jaz.task.second.exception.UserNotFoundException;
import pl.edu.pjwstk.jaz.task.second.repository.UsersRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@IntegrationTest
public class TestLoginController {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @MockBean
    UsersRepository usersRepository;
    @InjectMocks
    LoginController controller;

    @BeforeEach
    public void setup() {
        controller = new LoginController(usersRepository, new UserSession());
    }

    @Test
    public void shouldReturnCode200WhenPassCorrectDataToLogin() {
        UserDTO userAla = new UserDTO();
        userAla.setUsername("ala");
        userAla.setPassword("kot");
        userAla.setRole("");
        when(usersRepository.getUser("ala")).thenReturn((userAla));

        String json = "{ \"username\": \"ala\", \"password\": \"kot\", \"role\": \"admin\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/login")
                .then()
                .statusCode(equalTo(200))
                .body(equalTo("Logged in"));
    }

    @Test
    public void shouldReturnCode401WhenPassInCorrectDataToLogin() {
        UserDTO userAla = new UserDTO();
        userAla.setUsername("ala");
        userAla.setPassword("Kot");
        userAla.setRole("");
        when(usersRepository.getUser("ala")).thenReturn((userAla));

        String json = "{ \"username\": \"ala\", \"password\": \"wrongPassword\", \"role\": \"admin\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/login")
                .then()
                .statusCode(equalTo(401))
                .body(equalTo("UNAUTHORIZED"));
    }

    @Test
    public void shouldReturnCode401WhenPassUserDontExist() {
        doThrow(new UserNotFoundException("There is no user with this name")).when(usersRepository).getUser("ala");
        String json = "{ \"username\": \"ala\", \"password\": \"Kot\", \"role\": \"admin\" }";
        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/login")
                .then()
                .statusCode(equalTo(401))
                .body(equalTo("There is no user with this name"));
    }
}

