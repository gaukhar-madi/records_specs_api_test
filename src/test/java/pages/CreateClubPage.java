package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CreateClubPage {

    // Elements
    private SelenideElement titleInput = $("#bookTitle");
    private SelenideElement authorInput = $("#bookAuthors");
    private SelenideElement yearInput = $("#publicationYear");
    private SelenideElement descriptionInput = $("#description");
    private SelenideElement telegramChatLinkInput = $("#telegramChatLink");
    private SelenideElement submitButton = $(".submit-btn");

    // Actions
    @Step("Открываем браузер на странице создания клуба")
    public CreateClubPage openThisPage() {
        open("/clubs/create");

        return this;
    }

    @Step("Заполняем форму создания клуба")
    public CreateClubPage fillForm (String title, String author, String year, String description, String telegram) {
        titleInput.setValue(title);
        authorInput.setValue(author);
        yearInput.setValue(String.valueOf(year));
        descriptionInput.setValue(description);
        telegramChatLinkInput.setValue(telegram);

        return this;
    }

    @Step("Нажимаем кнопку \"Создать клуб\"")
    public CreateClubPage clickSubmitButton () {
        submitButton.click();

        return this;
    }


}