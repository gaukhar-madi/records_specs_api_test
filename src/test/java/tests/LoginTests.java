package tests;

import models.login.LoginBlankFieldsResponseModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class LoginTests extends tests.TestBase {

    @Test
    public void successfulLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        SuccessfulLoginResponseModel loginResponse = step("Авторизоваться с валидными логином и паролем", () ->
                api.auth.login(loginData));

        step("Проверить, что access и refresh токены получены и различаются", () -> {
            String actualAccess = loginResponse.access();
            String actualRefresh = loginResponse.refresh();
            assertThat(actualAccess).startsWith(LOGIN_TOKEN_PREFIX);
            assertThat(actualRefresh).startsWith(LOGIN_TOKEN_PREFIX);
            assertThat(actualAccess).isNotEqualTo(actualRefresh);
        });
    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_WRONG_PASSWORD);

        WrongCredentialsLoginResponseModel loginResponse = step("Авторизоваться с неверным паролем", () ->
                api.auth.loginWrongCredentials(loginData));

        step("Проверить сообщение об ошибке", () ->
                assertThat(loginResponse.detail()).isEqualTo(LOGIN_WRONG_CREDENTIALS_ERROR));
    }

    @Test
    public void loginWithNonExistentUsernameTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_WRONG_USERNAME, LOGIN_PASSWORD);

        WrongCredentialsLoginResponseModel loginResponse = step("Авторизоваться с несуществующим логином", () ->
                api.auth.loginWrongCredentials(loginData));

        step("Проверить сообщение об ошибке", () ->
                assertThat(loginResponse.detail()).isEqualTo(LOGIN_WRONG_CREDENTIALS_ERROR));
    }

    @Test
    public void loginWithEmptyUsernameTest() {
        LoginBodyModel loginData = new LoginBodyModel(EMPTY_STRING, LOGIN_PASSWORD);

        LoginBlankFieldsResponseModel loginResponse = step("Авторизоваться с пустым логином", () ->
                api.auth.loginBlankFields(loginData));

        step("Проверить сообщение об ошибке валидации поля username", () ->
                assertThat(loginResponse.username()).contains(FIELD_REQUIRED_ERROR));
    }

    @Test
    public void loginWithEmptyPasswordTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, EMPTY_STRING);

        LoginBlankFieldsResponseModel loginResponse = step("Авторизоваться с пустым паролем", () ->
                api.auth.loginBlankFields(loginData));

        step("Проверить сообщение об ошибке валидации поля password", () ->
                assertThat(loginResponse.password()).contains(FIELD_REQUIRED_ERROR));
    }

}
