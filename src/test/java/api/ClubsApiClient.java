package api;

import io.qameta.allure.Step;
import models.clubs.ClubBodyModel;
import models.clubs.ClubModel;
import models.clubs.ClubsListResponseModel;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.clubs.ClubsSpec.clubsRequestSpec;
import static specs.clubs.ClubsSpec.successfulClubCreateResponseSpec;
import static specs.clubs.ClubsSpec.successfulClubDeleteResponseSpec;
import static specs.clubs.ClubsSpec.successfulClubResponseSpec;
import static specs.clubs.ClubsSpec.successfulClubsListResponseSpec;

public class ClubsApiClient {

    @Step("Получение списка клубов GET /clubs/")
    public ClubsListResponseModel getClubs() {
        return given(clubsRequestSpec)
                .when()
                .get("/clubs/")
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    public ClubModel createClub(String accessToken, ClubBodyModel body) {
        return step("Создание клуба POST /clubs/", () ->
                given(clubsRequestSpec)
                        .auth().oauth2(accessToken)
                        .body(body)
                        .when()
                        .post("/clubs/")
                        .then()
                        .spec(successfulClubCreateResponseSpec)
                        .extract()
                        .as(ClubModel.class));
    }

    public ClubModel getClubById(Integer clubId) {
        return step("Получение клуба по id GET /clubs/{id}/", () ->
                given(clubsRequestSpec)
                        .when()
                        .get("/clubs/{id}/", clubId)
                        .then()
                        .spec(successfulClubResponseSpec)
                        .extract()
                        .as(ClubModel.class));
    }

    public int getClubStatusCode(Integer clubId) {
        return step("Запрос клуба по id для проверки статус-кода GET /clubs/{id}/", () ->
                given(clubsRequestSpec)
                        .when()
                        .get("/clubs/{id}/", clubId)
                        .then()
                        .extract()
                        .statusCode());
    }

    public ClubModel updateClubPut(String accessToken, Integer clubId, ClubBodyModel body) {
        return step("Обновление клуба через PUT /clubs/{id}/", () ->
                given(clubsRequestSpec)
                        .auth().oauth2(accessToken)
                        .body(body)
                        .when()
                        .put("/clubs/{id}/", clubId)
                        .then()
                        .spec(successfulClubResponseSpec)
                        .extract()
                        .as(ClubModel.class));
    }

    public void deleteClub(String accessToken, Integer clubId) {
        step("Удаление клуба DELETE /clubs/{id}/", () -> {
            given(clubsRequestSpec)
                    .auth().oauth2(accessToken)
                    .when()
                    .delete("/clubs/{id}/", clubId)
                    .then()
                    .spec(successfulClubDeleteResponseSpec);
        });
    }
}
