package tests;

import models.login.LoginBlankFieldsResponseModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class LoginTests extends tests.TestBase {

    @Test
    public void successfulLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();
        assertThat(actualAccess).startsWith(LOGIN_TOKEN_PREFIX);
        assertThat(actualRefresh).startsWith(LOGIN_TOKEN_PREFIX);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_WRONG_PASSWORD);

        WrongCredentialsLoginResponseModel loginResponse = api.auth.loginWrongCredentials(loginData);

        String expectedDetailError = LOGIN_WRONG_CREDENTIALS_ERROR;
        String actualDetailError = loginResponse.detail();
        assertThat(actualDetailError).isEqualTo(expectedDetailError);
    }

    @Test
    public void loginWithNonExistentUsernameTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_WRONG_USERNAME, LOGIN_PASSWORD);

        WrongCredentialsLoginResponseModel loginResponse = api.auth.loginWrongCredentials(loginData);

        String actualDetailError = loginResponse.detail();
        assertThat(actualDetailError).isEqualTo(LOGIN_WRONG_CREDENTIALS_ERROR);
    }

    @Test
    public void loginWithEmptyUsernameTest() {
        LoginBodyModel loginData = new LoginBodyModel(EMPTY_STRING, LOGIN_PASSWORD);

        LoginBlankFieldsResponseModel loginResponse = api.auth.loginBlankFields(loginData);

        assertThat(loginResponse.username()).contains(FIELD_REQUIRED_ERROR);
    }

    @Test
    public void loginWithEmptyPasswordTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, EMPTY_STRING);

        LoginBlankFieldsResponseModel loginResponse = api.auth.loginBlankFields(loginData);

        assertThat(loginResponse.password()).contains(FIELD_REQUIRED_ERROR);
    }

}