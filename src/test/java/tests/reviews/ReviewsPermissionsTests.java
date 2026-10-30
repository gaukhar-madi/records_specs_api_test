package tests.reviews;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import models.clubs.ClubReviewModel;
import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import models.reviews.ReviewBodyModel;
import models.reviews.ReviewDetailResponseModel;
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
@Story("Права доступа /api/v1/clubs/reviews/")
public class ReviewsPermissionsTests extends TestBase {

    private String authorToken;
    private Integer clubId;
    private ClubReviewModel authorReview;

    @BeforeEach
    public void createClubWithAuthorReview() {
        authorToken = api.auth.login(new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD)).access();

        clubId = api.clubs.createClub(authorToken, new TestDataFaker().clubBody()).id();
        authorReview = api.reviews.createReview(authorToken, new TestDataFaker().reviewBody(clubId));
    }

    @AfterEach
    public void deleteClubWithReviews() {
        // клуба нет, если подготовка упала до его создания
        if (clubId != null) {
            api.clubs.deleteClub(authorToken, clubId);
        }
    }

    @Test
    @DisplayName("Отзыв доступен для чтения без авторизации")
    public void getReviewWithoutAuthTest() {
        ClubReviewModel fetchedReview = api.reviews.getReviewById(authorReview.id());

        step("Проверить, что анонимный пользователь получил отзыв автора", () ->
                assertThat(fetchedReview).isEqualTo(authorReview));
    }

    @Test
    @DisplayName("Создание отзыва без авторизации")
    public void createReviewWithoutAuthTest() {
        ReviewBodyModel reviewData = new TestDataFaker().reviewBody(clubId);

        ReviewDetailResponseModel errorResponse = api.reviews.createReviewWithoutAuth(reviewData);

        step("Проверить сообщение об ошибке авторизации", () ->
                assertThat(errorResponse.detail()).isEqualTo(UNAUTHORIZED_ERROR));
    }

    @Test
    @DisplayName("Создание отзыва с недействительным access токеном")
    public void createReviewWithInvalidTokenTest() {
        ReviewBodyModel reviewData = new TestDataFaker().reviewBody(clubId);

        ReviewDetailResponseModel errorResponse =
                api.reviews.createReviewWithInvalidToken(INVALID_ACCESS_TOKEN, reviewData);

        step("Проверить сообщение об ошибке недействительного токена", () ->
                assertThat(errorResponse.detail()).isEqualTo(INVALID_ACCESS_TOKEN_ERROR));
    }

    @Test
    @DisplayName("Другой пользователь не может обновить чужой отзыв")
    public void updateAnotherUserReviewTest() {
        String anotherUserToken = registerAndLoginAnotherUser();
        ReviewBodyModel updateData = new TestDataFaker().reviewBody(clubId);

        ReviewDetailResponseModel errorResponse =
                api.reviews.updateReviewPutForbidden(anotherUserToken, authorReview.id(), updateData);

        step("Проверить сообщение об отсутствии прав", () ->
                assertThat(errorResponse.detail()).isEqualTo(FORBIDDEN_ERROR));

        ClubReviewModel reviewAfterUpdate = api.reviews.getReviewById(authorReview.id());

        step("Проверить, что отзыв автора не изменился", () ->
                assertThat(reviewAfterUpdate).isEqualTo(authorReview));
    }

    @Test
    @DisplayName("Обновление отзыва без авторизации")
    public void updateReviewWithoutAuthTest() {
        ReviewBodyModel updateData = new ReviewBodyModel(null, new TestDataFaker().fakerDescription, null, null);

        ReviewDetailResponseModel errorResponse =
                api.reviews.updateReviewPatchWithoutAuth(authorReview.id(), updateData);

        step("Проверить сообщение об ошибке авторизации", () ->
                assertThat(errorResponse.detail()).isEqualTo(UNAUTHORIZED_ERROR));

        ClubReviewModel reviewAfterUpdate = api.reviews.getReviewById(authorReview.id());

        step("Проверить, что отзыв автора не изменился", () ->
                assertThat(reviewAfterUpdate).isEqualTo(authorReview));
    }

    @Test
    @DisplayName("Другой пользователь не может удалить чужой отзыв")
    public void deleteAnotherUserReviewTest() {
        String anotherUserToken = registerAndLoginAnotherUser();

        ReviewDetailResponseModel errorResponse =
                api.reviews.deleteReviewForbidden(anotherUserToken, authorReview.id());

        step("Проверить сообщение об отсутствии прав", () ->
                assertThat(errorResponse.detail()).isEqualTo(FORBIDDEN_ERROR));

        ClubReviewModel reviewAfterDelete = api.reviews.getReviewById(authorReview.id());

        step("Проверить, что отзыв автора не удалён", () ->
                assertThat(reviewAfterDelete).isEqualTo(authorReview));
    }

    @Test
    @DisplayName("Удаление отзыва без авторизации")
    public void deleteReviewWithoutAuthTest() {
        ReviewDetailResponseModel errorResponse = api.reviews.deleteReviewWithoutAuth(authorReview.id());

        step("Проверить сообщение об ошибке авторизации", () ->
                assertThat(errorResponse.detail()).isEqualTo(UNAUTHORIZED_ERROR));

        ClubReviewModel reviewAfterDelete = api.reviews.getReviewById(authorReview.id());

        step("Проверить, что отзыв автора не удалён", () ->
                assertThat(reviewAfterDelete).isEqualTo(authorReview));
    }

    private String registerAndLoginAnotherUser() {
        TestDataFaker anotherUser = new TestDataFaker();

        api.users.register(new RegistrationBodyModel(anotherUser.fakerUserFirstName, anotherUser.fakerPassword));

        return api.auth.login(new LoginBodyModel(anotherUser.fakerUserFirstName, anotherUser.fakerPassword)).access();
    }
}
