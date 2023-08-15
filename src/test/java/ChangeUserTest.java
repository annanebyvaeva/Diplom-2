
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import user.UserRandom;
import user.UserSteps;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;

public class ChangeUserTest {
    private User user;
    private UserSteps userStep;
    String accessToken;

    @Before
    public void tearUp() {
        user = UserRandom.getRandom();
        userStep = new UserSteps();
        accessToken = userStep.registerUser(user).extract().header("Authorization");
    }

    @Test
    @Description("Проверка успешного изменения данных зарегистрированного пользователя")
    public void updateUserTest() {
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        ValidatableResponse response = userStep.updateUser(accessToken, user);
        response.assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true));
    }

    @Test
    @Description("Изменения данных незарегистрированого пользователя")
    public void updateNonAuthorizedUserTest() {
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        ValidatableResponse response = userStep.updateUser(user);
        response.assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }
}
