package api;

import io.qameta.allure.Step;
import models.login.LoginBlankFieldsResponseModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import models.logout.LogoutBlankFieldsResponseModel;
import models.logout.LogoutBodyModel;
import models.logout.LogoutInvalidTokenResponseModel;

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

    @Step("Авторизация с валидными данными POST /auth/token/")
    public SuccessfulLoginResponseModel login(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .as(SuccessfulLoginResponseModel.class);
    }

    @Step("Авторизация и получение refresh токена POST /auth/token/")
    public String loginAndGetRefreshToken(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .path("refresh");
    }

    @Step("Авторизация с неверными учетными данными POST /auth/token/")
    public WrongCredentialsLoginResponseModel loginWrongCredentials(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec)
                .extract()
                .as(WrongCredentialsLoginResponseModel.class);
    }

    @Step("Авторизация с пустыми полями POST /auth/token/")
    public LoginBlankFieldsResponseModel loginBlankFields(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(blankFieldsLoginResponseSpec)
                .extract()
                .as(LoginBlankFieldsResponseModel.class);
    }

    @Step("Выход из системы POST /auth/logout/")
    public void logout(LogoutBodyModel logoutBody) {
        given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    @Step("Выход из системы с пустым refresh токеном POST /auth/logout/")
    public LogoutBlankFieldsResponseModel logoutBlankFields(LogoutBodyModel logoutBody) {
        return given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(blankFieldsLogoutResponseSpec)
                .extract()
                .as(LogoutBlankFieldsResponseModel.class);
    }

    @Step("Выход из системы с недействительным refresh токеном POST /auth/logout/")
    public LogoutInvalidTokenResponseModel logoutInvalidToken(LogoutBodyModel logoutBody) {
        return given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(invalidTokenLogoutResponseSpec)
                .extract()
                .as(LogoutInvalidTokenResponseModel.class);
    }
}
