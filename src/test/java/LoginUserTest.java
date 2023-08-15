import io.restassured.response.ValidatableResponse;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserRandom;
import user.UserSteps;
import io.qameta.allure.Description;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;

public class LoginUserTest {
    private User user;
    private UserSteps userStep;
    String accessToken;

    @Before
    public void tearUp() {
        user = UserRandom.getRandom();
        userStep = new UserSteps();
        userStep.registerUser(user);
    }
    @Test
    @Description("Логин под существующим пользователем")
    public void loginUser() {
        ValidatableResponse response = userStep.loginUser(user);
        response.assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true));
        accessToken = response.extract().path("accessToken").toString();
    }
    @Test
    @Description("Логин пользователя c неверным логином")
    public void loginUserWithWrongEmailTest() {
        user.setEmail("");
        ValidatableResponse response = userStep.loginUser(user);
        response.assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }
    @Test
    @Description("Логин пользователя c неверным логином")
    public void loginUserWithWrongPasswordTest() {
        user.setPassword("");
        ValidatableResponse response = userStep.loginUser(user);
        response.assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }
    @After
    public void tearDown() {
        if (accessToken != null){
            userStep.deleteUser(accessToken);
        }
    }
}
