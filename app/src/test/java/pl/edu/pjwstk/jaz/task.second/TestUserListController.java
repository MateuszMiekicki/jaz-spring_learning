package pl.edu.pjwstk.jaz.task.second;

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
import pl.edu.pjwstk.jaz.task.second.component.restController.AccountController;
import pl.edu.pjwstk.jaz.task.second.exception.UserNotFoundException;
import pl.edu.pjwstk.jaz.task.second.repository.UsersRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@IntegrationTest
public class TestUserListController {
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
                .get("/api/userList")
                .then()
                .statusCode(200)
                .body(equalTo(userAla.toString() + ",\n" + userBartek.toString()));
    }

    @Test
    public void shouldReturnCode204AndShortMessageWhenTryGetInformationAboutUserDontExist() {
        doThrow(new UserNotFoundException("There is no user with this name")).when(usersRepository).getUser("ala");
        given()
                .param("username", "ala")
                .when()
                .get("/api/userList")
                .then()
                .statusCode(204)
                .body(equalTo(""));
    }

    @Test
    public void shouldReturnCode200AndShortMessageWhenNotPassAnything() {
        given()
                .param("username", "")
                .when()
                .get("/api/userList")
                .then()
                .statusCode(200)
                .body(equalTo("You must provide a user name."));
    }

}
