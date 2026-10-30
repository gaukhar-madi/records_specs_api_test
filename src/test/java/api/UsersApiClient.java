package api;

import io.qameta.allure.Step;
import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBlankFieldsResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import models.user.UnauthorizedResponseModel;
import models.user.UpdateUserBodyModel;
import models.user.UpdateUserFieldErrorResponseModel;
import models.user.UpdateUserResponseModel;

import static io.restassured.RestAssured.given;
import static specs.registration.RegistrationSpec.blankFieldsRegistrationResponseSpec;
import static specs.registration.RegistrationSpec.existingUserRegistrationResponseSpec;
import static specs.registration.RegistrationSpec.registrationRequestSpec;
import static specs.registration.RegistrationSpec.successfulRegistrationResponseSpec;
import static specs.user.UpdateUserSpec.fieldErrorUpdateUserResponseSpec;
import static specs.user.UpdateUserSpec.successfulGetUserResponseSpec;
import static specs.user.UpdateUserSpec.successfulUpdateUserResponseSpec;
import static specs.user.UpdateUserSpec.unauthorizedUpdateUserResponseSpec;
import static specs.user.UpdateUserSpec.updateUserRequestSpec;

public class UsersApiClient {

    @Step("Регистрация нового пользователя POST /users/register/")
    public SuccessfulRegistrationResponseModel register(RegistrationBodyModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);
    }

    @Step("Повторная регистрация существующего пользователя POST /users/register/")
    public ExistingUserResponseModel registerExistingUser(RegistrationBodyModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(existingUserRegistrationResponseSpec)
                .extract()
                .as(ExistingUserResponseModel.class);
    }

    @Step("Регистрация пользователя с пустыми полями POST /users/register/")
    public RegistrationBlankFieldsResponseModel registerBlankFields(RegistrationBodyModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(blankFieldsRegistrationResponseSpec)
                .extract()
                .as(RegistrationBlankFieldsResponseModel.class);
    }

    @Step("Получение профиля текущего пользователя GET /users/me/")
    public UpdateUserResponseModel getCurrentUser(String accessToken) {
        return given(updateUserRequestSpec)
                .auth().oauth2(accessToken)
                .when()
                .get("/users/me/")
                .then()
                .spec(successfulGetUserResponseSpec)
                .extract()
                .as(UpdateUserResponseModel.class);
    }

    @Step("Обновление профиля пользователя PUT /users/me/")
    public UpdateUserResponseModel updateUserPut(String accessToken, UpdateUserBodyModel body) {
        return given(updateUserRequestSpec)
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .put("/users/me/")
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract()
                .as(UpdateUserResponseModel.class);
    }

    @Step("Обновление профиля пользователя PATCH /users/me/")
    public UpdateUserResponseModel updateUserPatch(String accessToken, UpdateUserBodyModel body) {
        return given(updateUserRequestSpec)
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .patch("/users/me/")
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract()
                .as(UpdateUserResponseModel.class);
    }

    @Step("Обновление профиля PUT /users/me/ с отсутствующим обязательным полем")
    public UpdateUserFieldErrorResponseModel updateUserPutFieldError(String accessToken, UpdateUserBodyModel body) {
        return given(updateUserRequestSpec)
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .put("/users/me/")
                .then()
                .spec(fieldErrorUpdateUserResponseSpec)
                .extract()
                .as(UpdateUserFieldErrorResponseModel.class);
    }

    @Step("Обновление профиля PATCH /users/me/ с некорректным значением поля")
    public UpdateUserFieldErrorResponseModel updateUserPatchFieldError(String accessToken, UpdateUserBodyModel body) {
        return given(updateUserRequestSpec)
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .patch("/users/me/")
                .then()
                .spec(fieldErrorUpdateUserResponseSpec)
                .extract()
                .as(UpdateUserFieldErrorResponseModel.class);
    }

    @Step("Обновление профиля PATCH /users/me/ без заголовка Authorization")
    public UnauthorizedResponseModel updateUserPatchWithoutAuth(UpdateUserBodyModel body) {
        return given(updateUserRequestSpec)
                .body(body)
                .when()
                .patch("/users/me/")
                .then()
                .spec(unauthorizedUpdateUserResponseSpec)
                .extract()
                .as(UnauthorizedResponseModel.class);
    }
}
