package tests;

import models.registration.ExistingUserResponseModel;
import models.registration.RegistrationBlankFieldsResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.EMPTY_STRING;
import static tests.TestData.FIELD_REQUIRED_ERROR;
import static tests.TestData.REGISTRATION_EXISTING_USER_ERROR;
import static tests.TestData.REGISTRATION_IP_REGEXP;

public class RegistrationTests extends tests.TestBase {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        // оставляем генерацию данных в тесте, чтобы каждый запуск был с новыми пользователями
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
    }

    @Test
    public void successfulRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse =
                api.users.register(registrationData);

        assertThat(registrationResponse.id()).isGreaterThan(0);
        assertThat(registrationResponse.username()).isEqualTo(username);
        assertThat(registrationResponse.firstName()).isEqualTo("");
        assertThat(registrationResponse.lastName()).isEqualTo("");
        assertThat(registrationResponse.email()).isEqualTo("");

        assertThat(registrationResponse.remoteAddr()).matches(REGISTRATION_IP_REGEXP);
    }

    @Test
    public void existingUserWrongRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel firstRegistrationResponse =
                api.users.register(registrationData);

        assertThat(firstRegistrationResponse.username()).isEqualTo(username);

        ExistingUserResponseModel secondRegistrationResponse =
                api.users.registerExistingUser(registrationData);

        String expectedError = REGISTRATION_EXISTING_USER_ERROR;
        String actualError = secondRegistrationResponse.username().get(0);
        assertThat(actualError).isEqualTo(expectedError);
    }

    @Test
    public void registrationWithEmptyUsernameTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(EMPTY_STRING, password);

        RegistrationBlankFieldsResponseModel registrationResponse =
                api.users.registerBlankFields(registrationData);

        assertThat(registrationResponse.username()).contains(FIELD_REQUIRED_ERROR);
    }

    @Test
    public void registrationWithEmptyPasswordTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, EMPTY_STRING);

        RegistrationBlankFieldsResponseModel registrationResponse =
                api.users.registerBlankFields(registrationData);

        assertThat(registrationResponse.password()).contains(FIELD_REQUIRED_ERROR);
    }

}