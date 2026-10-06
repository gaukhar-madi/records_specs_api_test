package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.user.UnauthorizedResponseModel;
import models.user.UpdateUserBodyModel;
import models.user.UpdateUserFieldErrorResponseModel;
import models.user.UpdateUserResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class UpdateUserTests extends TestBase {

    private String accessToken;

    @BeforeEach
    public void loginAndGetAccessToken() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        SuccessfulLoginResponseModel loginResponse = step("Авторизоваться и получить access токен", () ->
                api.auth.login(loginData));

        accessToken = loginResponse.access();
    }

    @Test
    public void successfulUpdateUserViaPutTest() {
        UpdateUserBodyModel updateData =
                new UpdateUserBodyModel(LOGIN_USERNAME, UPDATED_FIRST_NAME, UPDATED_LAST_NAME, UPDATED_EMAIL);

        UpdateUserResponseModel updateResponse = step(
                "Обновить профиль пользователя через PUT со всеми полями",
                () -> api.users.updateUserPut(accessToken, updateData));

        step("Проверить, что username, имя, фамилия и email обновились", () -> {
            assertThat(updateResponse.username()).isEqualTo(LOGIN_USERNAME);
            assertThat(updateResponse.firstName()).isEqualTo(UPDATED_FIRST_NAME);
            assertThat(updateResponse.lastName()).isEqualTo(UPDATED_LAST_NAME);
            assertThat(updateResponse.email()).isEqualTo(UPDATED_EMAIL);
        });
    }

    @Test
    public void successfulUpdateUserViaPatchTest() {
        UpdateUserBodyModel updateData =
                new UpdateUserBodyModel(null, UPDATED_FIRST_NAME, null, null);

        UpdateUserResponseModel updateResponse = step(
                "Обновить только имя пользователя через PATCH",
                () -> api.users.updateUserPatch(accessToken, updateData));

        step("Проверить, что имя обновилось", () ->
                assertThat(updateResponse.firstName()).isEqualTo(UPDATED_FIRST_NAME));
    }

    @Test
    public void putUpdateUserWithMissingFieldTest() {
        UpdateUserBodyModel updateData =
                new UpdateUserBodyModel(LOGIN_USERNAME, null, UPDATED_LAST_NAME, UPDATED_EMAIL);

        UpdateUserFieldErrorResponseModel updateResponse = step(
                "Отправить PUT без обязательного поля firstName",
                () -> api.users.updateUserPutFieldError(accessToken, updateData));

        step("Проверить сообщение об ошибке отсутствующего поля firstName", () ->
                assertThat(updateResponse.firstName()).contains(FIELD_IS_REQUIRED_ERROR));
    }

    @Test
    public void updateUserWithInvalidEmailTest() {
        UpdateUserBodyModel updateData =
                new UpdateUserBodyModel(null, null, null, INVALID_EMAIL);

        UpdateUserFieldErrorResponseModel updateResponse = step(
                "Отправить PATCH с некорректным email",
                () -> api.users.updateUserPatchFieldError(accessToken, updateData));

        step("Проверить сообщение об ошибке валидации email", () ->
                assertThat(updateResponse.email()).contains(INVALID_EMAIL_ERROR));
    }

    @Test
    public void updateUserWithoutAuthTest() {
        UpdateUserBodyModel updateData =
                new UpdateUserBodyModel(null, UPDATED_FIRST_NAME, UPDATED_LAST_NAME, UPDATED_EMAIL);

        UnauthorizedResponseModel updateResponse = step(
                "Отправить PATCH без заголовка Authorization",
                () -> api.users.updateUserPatchWithoutAuth(updateData));

        step("Проверить сообщение об ошибке авторизации", () ->
                assertThat(updateResponse.detail()).isEqualTo(UNAUTHORIZED_ERROR));
    }
}
