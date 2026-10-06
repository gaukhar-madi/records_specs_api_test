package tests;

import models.login.LoginBodyModel;
import models.logout.LogoutBlankFieldsResponseModel;
import models.logout.LogoutBodyModel;
import models.logout.LogoutInvalidTokenResponseModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.EMPTY_STRING;
import static tests.TestData.FIELD_REQUIRED_ERROR;
import static tests.TestData.INVALID_REFRESH_TOKEN;
import static tests.TestData.INVALID_TOKEN_ERROR;
import static tests.TestData.LOGIN_PASSWORD;
import static tests.TestData.LOGIN_USERNAME;

public class LogoutTests extends tests.TestBase {

    @Test
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        String refreshToken = step("Авторизоваться и получить refresh токен", () ->
                api.auth.loginAndGetRefreshToken(loginData));

        LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);

        step("Выполнить logout с валидным refresh токеном", () -> api.auth.logout(logoutData));
    }

    @Test
    public void logoutWithInvalidRefreshTokenTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel(INVALID_REFRESH_TOKEN);

        LogoutInvalidTokenResponseModel logoutResponse = step(
                "Выполнить logout с недействительным refresh токеном",
                () -> api.auth.logoutInvalidToken(logoutData));

        step("Проверить сообщение об ошибке невалидного токена", () ->
                assertThat(logoutResponse.detail()).isEqualTo(INVALID_TOKEN_ERROR));
    }

    @Test
    public void logoutWithEmptyRefreshTokenTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel(EMPTY_STRING);

        LogoutBlankFieldsResponseModel logoutResponse = step(
                "Выполнить logout с пустым refresh токеном",
                () -> api.auth.logoutBlankFields(logoutData));

        step("Проверить сообщение об ошибке валидации поля refresh", () ->
                assertThat(logoutResponse.refresh()).contains(FIELD_REQUIRED_ERROR));
    }
}
