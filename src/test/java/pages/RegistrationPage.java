package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class RegistrationPage {

    // Elements
    private SelenideElement usernameInput = $("[data-testid=username-input]");
    private SelenideElement passwordInput = $("[data-testid=password-input]");
    private SelenideElement confirmPasswordInput = $("[data-testid=confirm-password-input]");
    private SelenideElement signupButton = $("[data-testid=signup-button]");
    private SelenideElement errorMessage = $("[data-testid=password-mismatch-error]");

    // Actions
    @Step("Открываем страницу регистрации")
    public RegistrationPage openRegistrationPage() {
        open("/signup");

        return this;
    }

    @Step("Заполняем поле логин")
    public RegistrationPage fillUsername(String username) {
        usernameInput.setValue(username);

        return this;
    }

    @Step("Заполняем поле пароль и подтверждаем")
    public RegistrationPage fillPass(String password) {
        passwordInput.setValue(password);
        confirmPasswordInput.setValue(password);

        return this;
    }

    @Step("Заполняем поле пароль и подтверждаем с ошибкой")
    public RegistrationPage fillPassWithWrongConfirm(String password) {
        passwordInput.setValue(password);
        confirmPasswordInput.setValue("password");

        return this;
    }

    @Step("Заполняем поле пароль и подтверждаем")
    public RegistrationPage clickSignupButton() {
        signupButton.click();

        return this;
    }

    @Step("Проверяем сообщение о ошибке в паролях")
    public RegistrationPage checkErrorMessage() {
        errorMessage.shouldBe(visible);

        return this;
    }
}