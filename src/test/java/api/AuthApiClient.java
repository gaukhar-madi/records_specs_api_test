package api;

import models.login.LoginBlankFieldsResponseModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import models.logout.LogoutBlankFieldsResponseModel;
import models.logout.LogoutBodyModel;
import models.logout.LogoutInvalidTokenResponseModel;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.login.LoginSpec.blankFieldsLoginResponseSpec;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.login.LoginSpec.wrongCredentialsLoginResponseSpec;
import static specs.logout.LogoutSpec.blankFieldsLogoutResponseSpec;
import static specs.logout.LogoutSpec.invalidTokenLogoutResponseSpec;
import static specs.logout.LogoutSpec.logoutRequestSpec;
import static specs.logout.LogoutSpec.successfulLogoutResponseSpec;

public class AuthApiClient {

    public SuccessfulLoginResponseModel login(LoginBodyModel loginBody) {
        return step("Авторизация с валидными данными", () ->
                given(loginRequestSpec)
                        .body(loginBody)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(successfulLoginResponseSpec)
                        .extract()
                        .as(SuccessfulLoginResponseModel.class));
    }

    public String loginAndGetRefreshToken(LoginBodyModel loginBody) {
        return step("Авторизация и получение токена", () ->
                given(loginRequestSpec)
                        .body(loginBody)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(successfulLoginResponseSpec)
                        .extract()
                        .path("refresh"));
    }

    public WrongCredentialsLoginResponseModel loginWrongCredentials(LoginBodyModel loginBody) {
        return step("Авторизация с неверными учетными данными", () ->
                given(loginRequestSpec)
                        .body(loginBody)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(wrongCredentialsLoginResponseSpec)
                        .extract()
                        .as(WrongCredentialsLoginResponseModel.class));
    }

    public LoginBlankFieldsResponseModel loginBlankFields(LoginBodyModel loginBody) {
        return step("Авторизация с пустыми полями", () ->
                given(loginRequestSpec)
                        .body(loginBody)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(blankFieldsLoginResponseSpec)
                        .extract()
                        .as(LoginBlankFieldsResponseModel.class));
    }

    public void logout(LogoutBodyModel logoutBody) {
        step("Отправка запроса logout", () -> {
            given(logoutRequestSpec)
                    .body(logoutBody)
                    .when()
                    .post("/auth/logout/")
                    .then()
                    .spec(successfulLogoutResponseSpec);
        });
    }

    public LogoutBlankFieldsResponseModel logoutBlankFields(LogoutBodyModel logoutBody) {
        return step("Отправка запроса logout с пустым refresh токеном", () ->
                given(logoutRequestSpec)
                        .body(logoutBody)
                        .when()
                        .post("/auth/logout/")
                        .then()
                        .spec(blankFieldsLogoutResponseSpec)
                        .extract()
                        .as(LogoutBlankFieldsResponseModel.class));
    }

    public LogoutInvalidTokenResponseModel logoutInvalidToken(LogoutBodyModel logoutBody) {
        return step("Отправка запроса logout с недействительным refresh токеном", () ->
                given(logoutRequestSpec)
                        .body(logoutBody)
                        .when()
                        .post("/auth/logout/")
                        .then()
                        .spec(invalidTokenLogoutResponseSpec)
                        .extract()
                        .as(LogoutInvalidTokenResponseModel.class));
    }
}
