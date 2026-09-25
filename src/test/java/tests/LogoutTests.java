package tests;

import models.login.LoginBodyModel;
import models.logout.LogoutBlankFieldsResponseModel;
import models.logout.LogoutBodyModel;
import models.logout.LogoutInvalidTokenResponseModel;
import org.junit.jupiter.api.Test;

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
        String refreshToken = api.auth.loginAndGetRefreshToken(loginData);

        LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
        api.auth.logout(logoutData);
    }

    @Test
    public void logoutWithInvalidRefreshTokenTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel(INVALID_REFRESH_TOKEN);

        LogoutInvalidTokenResponseModel logoutResponse = api.auth.logoutInvalidToken(logoutData);

        assertThat(logoutResponse.detail()).isEqualTo(INVALID_TOKEN_ERROR);
    }

    @Test
    public void logoutWithEmptyRefreshTokenTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel(EMPTY_STRING);

        LogoutBlankFieldsResponseModel logoutResponse = api.auth.logoutBlankFields(logoutData);

        assertThat(logoutResponse.refresh()).contains(FIELD_REQUIRED_ERROR);
    }
}