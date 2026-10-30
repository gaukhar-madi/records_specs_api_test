package api;

import io.qameta.allure.Step;
import models.clubs.ClubReviewModel;
import models.reviews.AddWrongReviewResponseModel;
import models.reviews.ReviewBodyModel;
import models.reviews.ReviewDetailResponseModel;
import models.reviews.ReviewsListResponseModel;

import static io.restassured.RestAssured.given;
import static specs.reviews.AddReviewSpec.addWrongReviewsSpec;
import static specs.reviews.AddReviewSpec.successfulAddReviewsSpec;
import static specs.reviews.DeleteReviewSpec.successfulDeleteReviewSpec;
import static specs.reviews.GetReviewSpec.getDeletedReviewsResponseSpec;
import static specs.reviews.GetReviewSpec.successfulGetReviewResponseSpec;
import static specs.reviews.GetReviewSpec.successfulGetReviewsListResponseSpec;
import static specs.reviews.ReviewsSpec.forbiddenReviewResponseSpec;
import static specs.reviews.ReviewsSpec.reviewsRequestSpec;
import static specs.reviews.ReviewsSpec.unauthorizedReviewResponseSpec;
import static specs.reviews.UpdateReviewSpec.successfulUpdatePutReviewsSpec;

public class ReviewsApiClient {

    @Step("Создание отзыва POST /clubs/reviews/")
    public ClubReviewModel createReview(String accessToken, ReviewBodyModel body) {
        return given(reviewsRequestSpec)
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(successfulAddReviewsSpec)
                .extract()
                .as(ClubReviewModel.class);
    }

    @Step("Создание отзыва без обязательных полей POST /clubs/reviews/")
    public AddWrongReviewResponseModel createReviewWithEmptyFields(String accessToken, ReviewBodyModel body) {
        return given(reviewsRequestSpec)
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(addWrongReviewsSpec)
                .extract()
                .as(AddWrongReviewResponseModel.class);
    }

    @Step("Создание отзыва без заголовка Authorization POST /clubs/reviews/")
    public ReviewDetailResponseModel createReviewWithoutAuth(ReviewBodyModel body) {
        return given(reviewsRequestSpec)
                .body(body)
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(unauthorizedReviewResponseSpec)
                .extract()
                .as(ReviewDetailResponseModel.class);
    }

    @Step("Создание отзыва с недействительным access токеном POST /clubs/reviews/")
    public ReviewDetailResponseModel createReviewWithInvalidToken(String invalidToken, ReviewBodyModel body) {
        return given(reviewsRequestSpec)
                .auth().oauth2(invalidToken)
                .body(body)
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(unauthorizedReviewResponseSpec)
                .extract()
                .as(ReviewDetailResponseModel.class);
    }

    @Step("Получение отзыва по id GET /clubs/reviews/{reviewId}/")
    public ClubReviewModel getReviewById(Integer reviewId) {
        return given(reviewsRequestSpec)
                .when()
                .get("/clubs/reviews/{id}/", reviewId)
                .then()
                .spec(successfulGetReviewResponseSpec)
                .extract()
                .as(ClubReviewModel.class);
    }

    @Step("Получение отзывов клуба GET /clubs/reviews/?club={clubId}")
    public ReviewsListResponseModel getReviewsByClub(Integer clubId) {
        return given(reviewsRequestSpec)
                .queryParam("club", clubId)
                .when()
                .get("/clubs/reviews/")
                .then()
                .spec(successfulGetReviewsListResponseSpec)
                .extract()
                .as(ReviewsListResponseModel.class);
    }

    @Step("Получение удалённого отзыва по id GET /clubs/reviews/{reviewId}/")
    public ReviewDetailResponseModel getDeletedReview(Integer reviewId) {
        return given(reviewsRequestSpec)
                .when()
                .get("/clubs/reviews/{id}/", reviewId)
                .then()
                .spec(getDeletedReviewsResponseSpec)
                .extract()
                .as(ReviewDetailResponseModel.class);
    }

    @Step("Обновление отзыва PUT /clubs/reviews/{reviewId}/")
    public ClubReviewModel updateReviewPut(String accessToken, Integer reviewId, ReviewBodyModel body) {
        return given(reviewsRequestSpec)
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .put("/clubs/reviews/{id}/", reviewId)
                .then()
                .spec(successfulUpdatePutReviewsSpec)
                .extract()
                .as(ClubReviewModel.class);
    }

    @Step("Обновление отзыва PATCH /clubs/reviews/{reviewId}/")
    public ClubReviewModel updateReviewPatch(String accessToken, Integer reviewId, ReviewBodyModel body) {
        return given(reviewsRequestSpec)
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .patch("/clubs/reviews/{id}/", reviewId)
                .then()
                .spec(successfulUpdatePutReviewsSpec)
                .extract()
                .as(ClubReviewModel.class);
    }

    @Step("Обновление чужого отзыва PUT /clubs/reviews/{reviewId}/")
    public ReviewDetailResponseModel updateReviewPutForbidden(String accessToken, Integer reviewId,
                                                              ReviewBodyModel body) {
        return given(reviewsRequestSpec)
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .put("/clubs/reviews/{id}/", reviewId)
                .then()
                .spec(forbiddenReviewResponseSpec)
                .extract()
                .as(ReviewDetailResponseModel.class);
    }

    @Step("Обновление отзыва PATCH /clubs/reviews/{reviewId}/ без заголовка Authorization")
    public ReviewDetailResponseModel updateReviewPatchWithoutAuth(Integer reviewId, ReviewBodyModel body) {
        return given(reviewsRequestSpec)
                .body(body)
                .when()
                .patch("/clubs/reviews/{id}/", reviewId)
                .then()
                .spec(unauthorizedReviewResponseSpec)
                .extract()
                .as(ReviewDetailResponseModel.class);
    }

    @Step("Удаление отзыва DELETE /clubs/reviews/{reviewId}/")
    public void deleteReview(String accessToken, Integer reviewId) {
        given(reviewsRequestSpec)
                .auth().oauth2(accessToken)
                .when()
                .delete("/clubs/reviews/{id}/", reviewId)
                .then()
                .spec(successfulDeleteReviewSpec);
    }

    @Step("Удаление чужого отзыва DELETE /clubs/reviews/{reviewId}/")
    public ReviewDetailResponseModel deleteReviewForbidden(String accessToken, Integer reviewId) {
        return given(reviewsRequestSpec)
                .auth().oauth2(accessToken)
                .when()
                .delete("/clubs/reviews/{id}/", reviewId)
                .then()
                .spec(forbiddenReviewResponseSpec)
                .extract()
                .as(ReviewDetailResponseModel.class);
    }

    @Step("Удаление отзыва DELETE /clubs/reviews/{reviewId}/ без заголовка Authorization")
    public ReviewDetailResponseModel deleteReviewWithoutAuth(Integer reviewId) {
        return given(reviewsRequestSpec)
                .when()
                .delete("/clubs/reviews/{id}/", reviewId)
                .then()
                .spec(unauthorizedReviewResponseSpec)
                .extract()
                .as(ReviewDetailResponseModel.class);
    }
}
