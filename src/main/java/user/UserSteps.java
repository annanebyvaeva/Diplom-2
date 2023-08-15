package user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.User;

import static io.restassured.RestAssured.given;
import static specification.BaseApi.spec;
import static specification.Endpoints.*;

public class UserSteps {
    @Step("Регистрация нового пользователя")
    public ValidatableResponse registerUser(User user) {
        return given().log().all()
                .spec(spec)
                .body(user)
                .when()
                .post(REGISTER_URI)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(User user) {
        return given().log().all()
                .spec(spec)
                .body(user)
                .when()
                .post(LOGIN_URI)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given().log().all()
                .spec(spec)
                .header("Authorization", accessToken)
                .when()
                .delete(USER_DATA_URI)
                .then();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse updateUser(String accessToken, User user) {
        return given().log().all()
                .spec(spec)
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(USER_DATA_URI)
                .then();
    }

    @Step("Изменение пользователя без токена")
    public ValidatableResponse updateUser(User user) {
        return given()
                .spec(spec)
                .body(user)
                .patch(USER_DATA_URI)
                .then();
    }

    @Step("Получение данных пользователя")
    public ValidatableResponse getUserData(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(spec)
                .when()
                .get(USER_DATA_URI)
                .then();
    }
}
