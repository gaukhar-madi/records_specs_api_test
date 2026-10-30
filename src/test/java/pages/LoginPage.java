package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {

    // Elements
    private SelenideElement usernameInput = $("[data-testid=username-input]");
    private SelenideElement passwordInput = $("[data-testid=password-input]");
    private SelenideElement submitButton = $("[data-testid=submit-button]");

    // Actions
    @Step("Открываем браузер на странице авторизации")
    public LoginPage openThisPage() {
        open("/signin");

        return this;
    }

    @Step("Заполняем поле логин")
    public LoginPage fillUsername(String username) {
        usernameInput.setValue(username);

        return this;
    }

    @Step("Заполняем поле пароль и подтверждаем")
    public LoginPage fillPass(String password) {
        passwordInput.setValue(password);

        return this;
    }

    @Step("Заполняем поле пароль и подтверждаем")
    public LoginPage clickSubmitButton() {
        submitButton.click();

        return this;
    }
}