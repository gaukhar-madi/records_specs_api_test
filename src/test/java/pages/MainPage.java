package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MainPage {

    // Elements
    private SelenideElement registrstionButton = $("[data-testid=signup-link]");
    private SelenideElement loginButton = $("[data-testid=signin-link]");
    private SelenideElement createClubButton = $("[data-testid=create-club-link]");
    private SelenideElement profileButton = $("[data-testid=profile-link]");
    private SelenideElement searchInput = $(".search-input");
    private SelenideElement searchButton = $(".search-button");
    private SelenideElement clubsTitle = $(".club-header");
    private SelenideElement yearClub = $(".year");
    private SelenideElement authorClub = $(".authors");
    private SelenideElement descriptionClub = $(".description");


    // Actions
    @Step("Открываем сайт на главной")
    public MainPage openMainPage() {
        open("");

        return this;
    }

    @Step("Открываем страницу регистрации")
    public MainPage openRegistrationPage() {
        registrstionButton.click();

        return this;
    }

    @Step("Открываем страницу авторизации")
    public MainPage openLoginPage() {
        loginButton.click();

        return this;
    }

    @Step("Проверяем кнопки доступные после регистрации")
    public MainPage checkButtonLoginUser() {
        createClubButton.shouldBe(visible);
        profileButton.shouldBe(visible);

        return this;
    }

    @Step("Находим клуб через поиск")
    public MainPage findClub(String bookTitle) {
        searchInput.setValue(bookTitle);
        searchButton.click();

        return this;
    }

    @Step("Проверяем карточку клуба")
    public MainPage checkClubCards(String title, String year, String author, String description) {
        clubsTitle.shouldHave(text(title));
        yearClub.shouldHave(text(year));
        authorClub.shouldHave(text(author));
        descriptionClub.shouldHave(text(description));

        return this;
    }
}