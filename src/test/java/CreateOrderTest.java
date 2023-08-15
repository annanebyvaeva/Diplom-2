import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import model.Order;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.OrderSteps;
import user.UserRandom;
import user.UserSteps;

import java.util.Arrays;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest {
    private Order order;
    private User user;
    private OrderSteps orderStep;
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
    @Description("Создание заказа с авторизацией и ингредиентами")
    public void createOrderWithIngredientsWithAuthTest() {
        order.setIngredients(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
        ValidatableResponse response = orderStep.CreateOrder(accessToken, order);
        response
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true));
    }

    @Test
    @Description("Создание заказа без авторизации с ингредиентами")
    public void createOrderWithIngredientsWithoutAuthTest() {
        order.setIngredients(Arrays.asList("61c0c5a71d1f82001bdaaa6f"));
        ValidatableResponse response = orderStep.CreateOrder("", order);
        response
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", is(true));
    }

    @Test
    @Description("Создание заказа без ингредиентов с авторизацией")
    public void createOrderEmptyIngredientsWithAuthTest() {
        order.setIngredients(null);
        ValidatableResponse response = orderStep.CreateOrder(accessToken, order);
        response
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .body("success", is(false))
                .and()
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @Description("Создание заказа с неверным хешем ингредиентов")
    public void createOrderInvalidIngredientsWithAuthTest() {
        order.setIngredients(Arrays.asList(RandomStringUtils.randomAlphanumeric(24)));
        ValidatableResponse response = orderStep.CreateOrder(accessToken, order);
        response
                .assertThat()
                .statusCode(HTTP_INTERNAL_ERROR);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userStep.deleteUser(accessToken);
        }
    }
}
