package tests.reviews;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import models.clubs.ClubModel;
import models.clubs.ClubReviewModel;
import models.login.LoginBodyModel;
import models.reviews.AddWrongReviewResponseModel;
import models.reviews.ReviewBodyModel;
import models.reviews.ReviewDetailResponseModel;
import models.reviews.ReviewsListResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test_base.TestBase;
import test_data.TestDataFaker;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static test_data.TestData.*;

@Feature("Отзывы")
@Story("CRUD /api/v1/clubs/reviews/")
public class ReviewsApiTests extends TestBase {

    private String accessToken;
    private Integer clubId;

    @BeforeEach
    public void createClubForReviews() {
        accessToken = api.auth.login(new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD)).access();

        clubId = api.clubs.createClub(accessToken, new TestDataFaker().clubBody()).id();
    }

    @AfterEach
    public void deleteClubWithReviews() {
        // клуба нет, если подготовка упала до его создания
        if (clubId != null) {
            api.clubs.deleteClub(accessToken, clubId);
        }
    }

    @Test
    @DisplayName("Создание отзыва")
    public void createReviewTest() {
        ReviewBodyModel reviewData = new TestDataFaker().reviewBody(clubId);

        ClubReviewModel createdReview = api.reviews.createReview(accessToken, reviewData);

        step("Проверить, что данные созданного отзыва совпадают с отправленными", () -> {
            assertThat(createdReview.id()).isNotNull();
            assertThat(createdReview.club()).isEqualTo(clubId);
            assertThat(createdReview.user().username()).isEqualTo(LOGIN_USERNAME);
            assertThat(createdReview.review()).isEqualTo(reviewData.review());
            assertThat(createdReview.assessment()).isEqualTo(reviewData.assessment());
            assertThat(createdReview.readPages()).isEqualTo(reviewData.readPages());
            assertThat(createdReview.modified()).isNull();
        });
    }

    @Test
    @DisplayName("Получение отзыва по id")
    public void getReviewByIdTest() {
        ClubReviewModel createdReview = api.reviews.createReview(accessToken, new TestDataFaker().reviewBody(clubId));

        ClubReviewModel fetchedReview = api.reviews.getReviewById(createdReview.id());

        step("Проверить, что полученный отзыв совпадает с созданным", () ->
                assertThat(fetchedReview).isEqualTo(createdReview));
    }

    @Test
    @DisplayName("Отзыв отображается в списке отзывов клуба и в карточке клуба")
    public void getClubReviewsTest() {
        ClubReviewModel createdReview = api.reviews.createReview(accessToken, new TestDataFaker().reviewBody(clubId));

        ReviewsListResponseModel clubReviews = api.reviews.getReviewsByClub(clubId);
        ClubModel club = api.clubs.getClubById(clubId);

        step("Проверить, что в списке отзывов клуба только созданный отзыв", () -> {
            assertThat(clubReviews.count()).isEqualTo(1);
            assertThat(clubReviews.results()).containsExactly(createdReview);
        });
        step("Проверить, что отзыв есть в карточке клуба", () ->
                assertThat(club.reviews()).containsExactly(createdReview));
    }

    @Test
    @DisplayName("Обновление отзыва методом PUT")
    public void updateReviewPutTest() {
        ClubReviewModel createdReview = api.reviews.createReview(accessToken, new TestDataFaker().reviewBody(clubId));
        ReviewBodyModel updateData = new TestDataFaker().reviewBody(clubId);

        ClubReviewModel updatedReview = api.reviews.updateReviewPut(accessToken, createdReview.id(), updateData);

        step("Проверить, что текст, оценка и количество страниц обновились", () -> {
            assertThat(updatedReview.id()).isEqualTo(createdReview.id());
            assertThat(updatedReview.review()).isEqualTo(updateData.review());
            assertThat(updatedReview.assessment()).isEqualTo(updateData.assessment());
            assertThat(updatedReview.readPages()).isEqualTo(updateData.readPages());
            assertThat(updatedReview.created()).isEqualTo(createdReview.created());
            assertThat(updatedReview.modified()).isNotNull();
        });
    }

    @Test
    @DisplayName("Частичное обновление отзыва методом PATCH")
    public void updateReviewPatchTest() {
        ClubReviewModel createdReview = api.reviews.createReview(accessToken, new TestDataFaker().reviewBody(clubId));
        String newReviewText = new TestDataFaker().fakerDescription;
        ReviewBodyModel updateData = new ReviewBodyModel(null, newReviewText, null, null);

        ClubReviewModel updatedReview = api.reviews.updateReviewPatch(accessToken, createdReview.id(), updateData);

        step("Проверить, что обновился только текст отзыва", () -> {
            assertThat(updatedReview.review()).isEqualTo(newReviewText);
            assertThat(updatedReview.assessment()).isEqualTo(createdReview.assessment());
            assertThat(updatedReview.readPages()).isEqualTo(createdReview.readPages());
            assertThat(updatedReview.club()).isEqualTo(clubId);
            assertThat(updatedReview.modified()).isNotNull();
        });
    }

    @Test
    @DisplayName("Удаление отзыва")
    public void deleteReviewTest() {
        ClubReviewModel createdReview = api.reviews.createReview(accessToken, new TestDataFaker().reviewBody(clubId));

        api.reviews.deleteReview(accessToken, createdReview.id());

        ReviewDetailResponseModel deletedReview = api.reviews.getDeletedReview(createdReview.id());

        step("Проверить, что отзыв больше не найден после удаления", () ->
                assertThat(deletedReview.detail()).isNotBlank());
    }

    @Test
    @DisplayName("Создание отзыва без обязательных полей")
    public void createReviewWithEmptyFieldsTest() {
        ReviewBodyModel emptyReviewData = new ReviewBodyModel(null, null, null, null);

        AddWrongReviewResponseModel errorResponse = api.reviews.createReviewWithEmptyFields(accessToken, emptyReviewData);

        step("Проверить сообщения об ошибке для полей club, review и assessment", () -> {
            assertThat(errorResponse.club()).contains(FIELD_IS_REQUIRED_ERROR);
            assertThat(errorResponse.review()).contains(FIELD_IS_REQUIRED_ERROR);
            assertThat(errorResponse.assessment()).contains(FIELD_IS_REQUIRED_ERROR);
        });
    }
}
