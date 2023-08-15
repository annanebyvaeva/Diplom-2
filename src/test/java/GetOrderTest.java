import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import model.Order;
import model.User;
import org.junit.Before;
import org.junit.Test;
import user.OrderSteps;
import user.UserRandom;
import user.UserSteps;

import java.util.Arrays;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;

public class GetOrderTest {
    private Order order;
    private OrderSteps orderStep;
    private User user;
    private UserSteps userStep;
    private String accessToken;

    @Before
    public void tearUp() {
        user = UserRandom.getRandom();
        orderStep = new OrderSteps();
        userStep = new UserSteps();
        order = new Order();
        accessToken = userStep.registerUser(user).extract().path("accessToken");

    }
    @Test
    @Description("Получение заказа пользователя с авторизацией")
    public void getOrderWithAuthTest() {
        order.setIngredients(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
        orderStep.CreateOrder(accessToken, order);
        ValidatableResponse response = orderStep.getOrdersUserTest(accessToken);
        response
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true));
    }
    @Test
    @Description("Получение заказа пользователя без авторизации")
    public void getOrderWithoutAuthTest() {
        order.setIngredients(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
        orderStep.CreateOrder(accessToken, order);
        ValidatableResponse response = orderStep.getOrdersUserTest("");
        response
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .body("message",is("You should be authorised"));;
    }
}
