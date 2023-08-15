package user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Order;
import static specification.BaseApi.spec;

import static io.restassured.RestAssured.given;
import static specification.Endpoints.ORDER_URI;

public class OrderSteps {
    @Step("Создание заказа")
    public ValidatableResponse CreateOrder(String accessToken, Order order){
        return given().log().all()
                .header("authorization",accessToken)
                .spec(spec)
                .body(order)
                .when()
                .post(ORDER_URI)
                .then();
    }
    @Step("Получение заказов")
    public ValidatableResponse getOrdersUserTest(String accessToken){
        return given()
                .header("authorization",accessToken)
                .spec(spec)
                .when()
                .get(ORDER_URI)
                .then();
    }
}
