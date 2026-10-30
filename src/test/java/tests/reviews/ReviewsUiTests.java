package tests.reviews;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import models.clubs.ClubReviewModel;
import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import models.reviews.ReviewsListResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ClubsPage;
import test_base.UiTestBase;
import test_data.TestDataFaker;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static test_data.TestData.LOGIN_PASSWORD;
import static test_data.TestData.LOGIN_USERNAME;

@Feature("Отзывы")
@Story("UI: отзывы на странице клуба")
public class ReviewsUiTests extends UiTestBase {

    private final ClubsPage clubsPage = new ClubsPage();

    private String accessToken;
    private Integer clubId;

    @BeforeEach
    public void loginAndCreateClubViaApi() {
        accessToken = loginViaApi(LOGIN_USERNAME, LOGIN_PASSWORD);

        clubId = api.clubs.createClub(accessToken, new TestDataFaker().clubBody()).id();
    }

    @AfterEach
    public void deleteClubViaApi() {
        // клуба нет, если подготовка упала до его создания
        if (clubId != null) {
            api.clubs.deleteClub(accessToken, clubId);
        }
    }

    @Test
    @DisplayName("Добавление отзыва через UI")
    public void addReviewTest() {
        TestDataFaker review = new TestDataFaker();

        clubsPage.openClubPage(clubId)
                .checkNoReviewMessage()
                .openReviewForm()
                .fillAssessment(review.fakerAssessment)
                .fillReadPages(review.fakerReadPages)
                .fillReview(review.fakerDescription)
                .publishReview()
                .checkReviewCards(LOGIN_USERNAME, review.fakerAssessment, review.fakerReadPages,
                        review.fakerDescription)
                // счётчик отзывов в шапке клуба обновляется только после перезагрузки страницы
                .refreshPage()
                .checkAmountReviews("1");

        ReviewsListResponseModel clubReviews = api.reviews.getReviewsByClub(clubId);

        step("Проверить через API, что отзыв сохранился", () -> {
            assertThat(clubReviews.count()).isEqualTo(1);
            assertThat(clubReviews.results().get(0).review()).isEqualTo(review.fakerDescription);
        });
    }

    @Test
    @DisplayName("Редактирование отзыва через UI")
    public void editReviewTest() {
        ClubReviewModel createdReview = api.reviews.createReview(accessToken, new TestDataFaker().reviewBody(clubId));
        TestDataFaker newReview = new TestDataFaker();

        clubsPage.openClubPage(clubId)
                .editReview()
                .fillAssessment(newReview.fakerAssessment)
                .fillReadPages(newReview.fakerReadPages)
                .fillReview(newReview.fakerDescription)
                .publishReview()
                .checkReviewCards(LOGIN_USERNAME, newReview.fakerAssessment, newReview.fakerReadPages,
                        newReview.fakerDescription)
                .checkAmountReviews("1");

        ClubReviewModel updatedReview = api.reviews.getReviewById(createdReview.id());

        step("Проверить через API, что отзыв обновился", () -> {
            assertThat(updatedReview.review()).isEqualTo(newReview.fakerDescription);
            assertThat(updatedReview.assessment()).isEqualTo(newReview.fakerAssessment);
            assertThat(updatedReview.readPages()).isEqualTo(newReview.fakerReadPages);
            assertThat(updatedReview.modified()).isNotNull();
        });
    }

    @Test
    @DisplayName("Удаление отзыва через UI")
    public void deleteReviewTest() {
        api.reviews.createReview(accessToken, new TestDataFaker().reviewBody(clubId));

        clubsPage.openClubPage(clubId)
                .checkAmountReviews("1")
                .deleteReview()
                .checkNoReviewMessage()
                // счётчик отзывов в шапке клуба обновляется только после перезагрузки страницы
                .refreshPage()
                .checkAmountReviews("0");

        ReviewsListResponseModel clubReviews = api.reviews.getReviewsByClub(clubId);

        step("Проверить через API, что у клуба нет отзывов", () ->
                assertThat(clubReviews.count()).isZero());
    }

    @Test
    @DisplayName("Нельзя редактировать и удалять чужой отзыв через UI")
    public void anotherUserReviewActionsNotVisibleTest() {
        TestDataFaker anotherUser = new TestDataFaker();
        api.users.register(new RegistrationBodyModel(anotherUser.fakerUserFirstName, anotherUser.fakerPassword));
        String anotherUserToken = api.auth
                .login(new LoginBodyModel(anotherUser.fakerUserFirstName, anotherUser.fakerPassword))
                .access();
        ClubReviewModel anotherUserReview =
                api.reviews.createReview(anotherUserToken, new TestDataFaker().reviewBody(clubId));

        clubsPage.openClubPage(clubId)
                .checkReviewCards(anotherUser.fakerUserFirstName, anotherUserReview.assessment(),
                        anotherUserReview.readPages(), anotherUserReview.review())
                .checkReviewActionsNotVisible();
    }
}
