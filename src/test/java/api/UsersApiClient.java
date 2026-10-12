package api;

import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBlankFieldsResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import models.user.UnauthorizedResponseModel;
import models.user.UpdateUserBodyModel;
import models.user.UpdateUserFieldErrorResponseModel;
import models.user.UpdateUserResponseModel;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.registration.RegistrationSpec.blankFieldsRegistrationResponseSpec;
import static specs.registration.RegistrationSpec.existingUserRegistrationResponseSpec;
import static specs.registration.RegistrationSpec.registrationRequestSpec;
import static specs.registration.RegistrationSpec.successfulRegistrationResponseSpec;
import static specs.user.UpdateUserSpec.fieldErrorUpdateUserResponseSpec;
import static specs.user.UpdateUserSpec.successfulUpdateUserResponseSpec;
import static specs.user.UpdateUserSpec.unauthorizedUpdateUserResponseSpec;
import static specs.user.UpdateUserSpec.updateUserRequestSpec;

public class UsersApiClient {

    public SuccessfulRegistrationResponseModel register(RegistrationBodyModel body) {
        return step("Регистрация нового пользователя", () ->
                given(registrationRequestSpec)
                        .body(body)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(successfulRegistrationResponseSpec)
                        .extract()
                        .as(SuccessfulRegistrationResponseModel.class));
    }

    public ExistingUserResponseModel registerExistingUser(RegistrationBodyModel body) {
        return step("Повторная регистрация уже существующего пользователя", () ->
                given(registrationRequestSpec)
                        .body(body)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(existingUserRegistrationResponseSpec)
                        .extract()
                        .as(ExistingUserResponseModel.class));
    }

    public RegistrationBlankFieldsResponseModel registerBlankFields(RegistrationBodyModel body) {
        return step("Регистрация пользователя с пустыми полями", () ->
                given(registrationRequestSpec)
                        .body(body)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(blankFieldsRegistrationResponseSpec)
                        .extract()
                        .as(RegistrationBlankFieldsResponseModel.class));
    }

    public UpdateUserResponseModel updateUserPut(String accessToken, UpdateUserBodyModel body) {
        return step("Обновление профиля пользователя через PUT", () ->
                given(updateUserRequestSpec)
                        .auth().oauth2(accessToken)
                        .body(body)
                        .when()
                        .put("/users/me/")
                        .then()
                        .spec(successfulUpdateUserResponseSpec)
                        .extract()
                        .as(UpdateUserResponseModel.class));
    }

    public UpdateUserResponseModel updateUserPatch(String accessToken, UpdateUserBodyModel body) {
        return step("Обновление профиля пользователя через PATCH", () ->
                given(updateUserRequestSpec)
                        .auth().oauth2(accessToken)
                        .body(body)
                        .when()
                        .patch("/users/me/")
                        .then()
                        .spec(successfulUpdateUserResponseSpec)
                        .extract()
                        .as(UpdateUserResponseModel.class));
    }

    public UpdateUserFieldErrorResponseModel updateUserPutFieldError(String accessToken, UpdateUserBodyModel body) {
        return step("Обновление профиля через PUT с отсутствующим обязательным полем", () ->
                given(updateUserRequestSpec)
                        .auth().oauth2(accessToken)
                        .body(body)
                        .when()
                        .put("/users/me/")
                        .then()
                        .spec(fieldErrorUpdateUserResponseSpec)
                        .extract()
                        .as(UpdateUserFieldErrorResponseModel.class));
    }

    public UpdateUserFieldErrorResponseModel updateUserPatchFieldError(String accessToken, UpdateUserBodyModel body) {
        return step("Обновление профиля через PATCH с некорректным значением поля", () ->
                given(updateUserRequestSpec)
                        .auth().oauth2(accessToken)
                        .body(body)
                        .when()
                        .patch("/users/me/")
                        .then()
                        .spec(fieldErrorUpdateUserResponseSpec)
                        .extract()
                        .as(UpdateUserFieldErrorResponseModel.class));
    }

    public UnauthorizedResponseModel updateUserPatchWithoutAuth(UpdateUserBodyModel body) {
        return step("Обновление профиля через PATCH без заголовка Authorization", () ->
                given(updateUserRequestSpec)
                        .body(body)
                        .when()
                        .patch("/users/me/")
                        .then()
                        .spec(unauthorizedUpdateUserResponseSpec)
                        .extract()
                        .as(UnauthorizedResponseModel.class));
    }
}
