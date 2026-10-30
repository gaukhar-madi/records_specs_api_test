package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static helpers.Helper.getNowDate;
import static helpers.Helper.getStars;

public class ClubsPage {

    // Elements
    private SelenideElement clubContent = $(".club-content");
    private SelenideElement clubsTitle = $(".club-header");
    private SelenideElement yearClub = $(".year");
    private SelenideElement authorClub = $(".authors");
    private SelenideElement descriptionClub = $(".description");
    private SelenideElement leaveButton = $(".leave-btn");
    private SelenideElement addReviewButton = $(".add-review-btn");
    private SelenideElement deleteReviewButton = $(".delete-review-btn");
    private SelenideElement editReviewButton = $(".edit-review-btn");
    private SelenideElement joinButton = $(".join-btn");
    private SelenideElement errorMessage = $(".error");
    private SelenideElement noReviewMessage = $(".no-reviews p");
    private SelenideElement assessmentInput = $("#assessment");
    private SelenideElement readPagesInput = $("#readPages");
    private SelenideElement reviewInput = $("#review");
    private SelenideElement publishButton = $(".save-btn");
    private SelenideElement amountReviews = $$(".stat-item").findBy(text("Отзывов:")).$(".stat-value");
    private SelenideElement reviewerName = $(".reviewer-name");
    private SelenideElement stars = $(".stars");
    private SelenideElement readsPages = $(".read-pages");
    private SelenideElement textReview = $(".review-content p");
    private SelenideElement dateReview = $(".review-date");

    // Actions
    @Step("Открываем страницу клуба")
    public ClubsPage openClubPage(Integer id) {
        open("/clubs/" + id);

        return this;
    }

    @Step("Отписываемся от клуба")
    public ClubsPage leaveClub() {
        leaveButton.click();

        return this;
    }

    @Step("Удаляем комментарий")
    public ClubsPage deleteReview() {
        deleteReviewButton.click();
        confirm("Вы уверены, что хотите удалить этот отзыв?");

        return this;
    }

    @Step("Редактировать комментарий")
    public ClubsPage editReview() {
        editReviewButton.click();

        return this;
    }

    @Step("Открыть форму ")
    public ClubsPage openReviewForm() {
        addReviewButton.click();

        return this;
    }

    @Step("Заполняем поле \"Оценка\"")
    public ClubsPage fillAssessment(Integer assessment) {
        // в новой форме уже стоит оценка по умолчанию, и setValue дописывает к ней цифру ("5" + "4" = "54"),
        // поэтому значение выставляется напрямую и отправляются события, на которые подписан v-model
        assessmentInput.shouldBe(visible);
        executeJavaScript(
                "arguments[0].value = arguments[1];"
                        + "arguments[0].dispatchEvent(new Event('input', {bubbles: true}));"
                        + "arguments[0].dispatchEvent(new Event('change', {bubbles: true}));",
                assessmentInput, String.valueOf(assessment));

        return this;
    }

    @Step("Заполняем поле \"Прочитано страниц\"")
    public ClubsPage fillReadPages(Integer readPages) {
        readPagesInput.setValue(String.valueOf(readPages));

        return this;
    }

    @Step("Заполняем поле \"Ваш отзыв\"")
    public ClubsPage fillReview(String review) {
        reviewInput.setValue(review);

        return this;
    }

    @Step("Опубликовать отзыв")
    public ClubsPage publishReview() {
        publishButton.click();

        return this;
    }

    @Step("Обновляем страницу клуба")
    public ClubsPage refreshPage() {
        refresh();

        return this;
    }

    @Step("Проверяем карточку клуба")
    public ClubsPage checkClubCards(String title, String author, String year, String description) {
        clubContent.shouldBe(visible);
        clubsTitle.shouldHave(text(title));
        yearClub.shouldHave(text(year));
        authorClub.shouldHave(text(author));
        descriptionClub.shouldHave(text(description));

        return this;
    }

    @Step("Проверяем карточку отзыва")
    public ClubsPage checkReviewCards(String reviewer, Integer assessment, Integer readPages, String description) {
        reviewerName.shouldHave(text(reviewer));
        stars.shouldHave(text(getStars(assessment)));
        readsPages.shouldHave(text(readPages + " стр."));
        textReview.shouldHave(text(description));
        dateReview.shouldHave(text(getNowDate()));

        return this;
    }

    @Step("Проверяем количество отзывов")
    public ClubsPage checkAmountReviews(String amount) {
        amountReviews.shouldHave(text(amount));

        return this;
    }

    @Step("Проверяем текс ошибки выхода из своего клуба")
    public ClubsPage checkErrorMessage() {
        errorMessage.shouldHave(text("Не удалось покинуть клуб"));

        return this;
    }

    @Step("Проверяем отсутствие отзывов у клуба")
    public ClubsPage checkNoReviewMessage() {
        noReviewMessage.shouldHave(text("Пока нет отзывов. Будьте первым, кто поделится впечатлениями!"));

        return this;
    }

    @Step("Проверяем что не состоим в клубе")
    public ClubsPage checkHasNotJoinClub() {
        joinButton.shouldBe(visible);

        return this;
    }

    @Step("Проверяем, что кнопки редактирования и удаления отзыва недоступны")
    public ClubsPage checkReviewActionsNotVisible() {
        editReviewButton.shouldNotBe(visible);
        deleteReviewButton.shouldNotBe(visible);

        return this;
    }


}