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
import pl.edu.pjwstk.jaz.task.second.component.UserSession;
import pl.edu.pjwstk.jaz.task.second.component.restController.UserListController;
import pl.edu.pjwstk.jaz.task.second.exception.UserNotFoundException;
import pl.edu.pjwstk.jaz.task.second.filters.AuthenticationFilter;
import pl.edu.pjwstk.jaz.task.second.filters.AuthorizationFilter;
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

    @MockBean
    UserSession session;

    @Test
    public void shouldReturnCode200AndUserDetailsWhenPassUsername() {
        UserDTO userAla = new UserDTO();
        userAla.setUsername("ala");
        userAla.setPassword("kot");
        when(usersRepository.getUser("ala")).thenReturn(userAla);

        UserDTO userBartek = new UserDTO();
        userBartek.setUsername("ala");
        userBartek.setPassword("kot");
        when(usersRepository.getUser("bartek")).thenReturn(userBartek);

        when(session.isLoggedIn()).thenReturn(true);
        when(session.getRole()).thenReturn("admin");

        given()
                .param("username", "ala,bartek")
                .when()
                .get("/api/userList")
                .then()
                .statusCode(200)
                .body(equalTo(userAla.toString() + ",\n" + userBartek.toString()));
    }

    @Test
    public void shouldReturnCode401WhenRequestIsMadeByUserWithoutRole() {
        when(session.isLoggedIn()).thenReturn(true);
        when(session.getRole()).thenReturn("user");
        given()
                .param("username", "ala,bartek")
                .when()
                .get("/api/userList")
                .then()
                .statusCode(401);
    }

    @Test
    public void shouldReturnCode204AndShortMessageWhenTryGetInformationAboutUserDontExist() {
        when(session.isLoggedIn()).thenReturn(true);
        when(session.getRole()).thenReturn("admin");
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
        when(session.isLoggedIn()).thenReturn(true);
        when(session.getRole()).thenReturn("admin");
        given()
                .param("username", "")
                .when()
                .get("/api/userList")
                .then()
                .statusCode(200)
                .body(equalTo("You must provide a user name."));
    }

}
