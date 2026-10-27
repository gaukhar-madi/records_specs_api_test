package api;

import io.qameta.allure.Step;
import models.clubs.ClubBodyModel;
import models.clubs.ClubModel;
import models.clubs.ClubsListResponseModel;

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

    @Step("Создание клуба POST /clubs/")
    public ClubModel createClub(String accessToken, ClubBodyModel body) {
        return given(clubsRequestSpec)
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .post("/clubs/")
                .then()
                .spec(successfulClubCreateResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Получение клуба по id GET /clubs/{clubId}/")
    public ClubModel getClubById(Integer clubId) {
        return given(clubsRequestSpec)
                .when()
                .get("/clubs/{id}/", clubId)
                .then()
                .spec(successfulClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Запрос клуба по id для получения статус-кода GET /clubs/{clubId}/")
    public int getClubStatusCode(Integer clubId) {
        return given(clubsRequestSpec)
                .when()
                .get("/clubs/{id}/", clubId)
                .then()
                .extract()
                .statusCode();
    }

    @Step("Обновление клуба PUT /clubs/{clubId}/")
    public ClubModel updateClubPut(String accessToken, Integer clubId, ClubBodyModel body) {
        return given(clubsRequestSpec)
                .auth().oauth2(accessToken)
                .body(body)
                .when()
                .put("/clubs/{id}/", clubId)
                .then()
                .spec(successfulClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Удаление клуба DELETE /clubs/{clubId}/")
    public void deleteClub(String accessToken, Integer clubId) {
        given(clubsRequestSpec)
                .auth().oauth2(accessToken)
                .when()
                .delete("/clubs/{id}/", clubId)
                .then()
                .spec(successfulClubDeleteResponseSpec);
    }
}
