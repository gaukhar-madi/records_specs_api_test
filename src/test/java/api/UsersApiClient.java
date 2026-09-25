package api;

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
import static specs.user.UpdateUserSpec.successfulUpdateUserResponseSpec;
import static specs.user.UpdateUserSpec.unauthorizedUpdateUserResponseSpec;
import static specs.user.UpdateUserSpec.updateUserRequestSpec;

public class UsersApiClient {

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
