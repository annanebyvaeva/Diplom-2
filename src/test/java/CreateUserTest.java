import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Description;
import user.UserRandom;
import user.UserSteps;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static org.hamcrest.CoreMatchers.is;
import static java.net.HttpURLConnection.HTTP_OK;

public class CreateUserTest {
    private User user;
    private UserSteps userStep;
    private String accessToken;

    @Before
    public void tearUp() {
        user = UserRandom.getRandom();
        userStep = new UserSteps();
    }

    @Test
    @Description("Создание нового уникального пользователя")
    public void createUniqUserTest() {
        ValidatableResponse response = userStep.registerUser(user);
        response.assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true));
        accessToken = response.extract().path("accessToken");
    }
    @Test
    @Description("Создание существующего пользователя")
    public void createNoUniqUserTest(){
        userStep.registerUser(user);
        ValidatableResponse response = userStep.registerUser(user);
        response.assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", is("User already exists"));
    }
    @Test
    @Description("Создание пользователя без имени")
    public void createUserWithoutNameFieldTest() {
        user.setName("");
        ValidatableResponse response = userStep.registerUser(user);
        response.assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @Description("Создание пользователя без электронной почты")
    public void createUserWithoutEmailFieldTest() {
        user.setEmail("");
        ValidatableResponse response = userStep.registerUser(user);
        response.assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }


    @Test
    @Description("Создание нового пользователя без пароля")
    public void createUserWithoutPasswordFieldTest() {
        user.setPassword("");
        ValidatableResponse response = userStep.registerUser(user);
        response.assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }
    @After
    public void cleanUp(){
        if (accessToken != null){
            userStep.deleteUser(accessToken);
        }
    }
}
