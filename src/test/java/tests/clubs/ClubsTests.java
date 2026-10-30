package tests.clubs;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import models.clubs.ClubBodyModel;
import models.clubs.ClubModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test_base.TestBase;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static test_data.TestData.*;

@Feature("Клубы")
@Story("CRUD /api/v1/clubs/")
public class ClubsTests extends TestBase {

    private String accessToken;

    @BeforeEach
    public void loginAndGetAccessToken() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        accessToken = loginResponse.access();
    }

    @Test
    @DisplayName("Создание клуба")
    public void createClubTest() {
        ClubBodyModel clubData = new ClubBodyModel(
                NEW_CLUB_BOOK_TITLE,
                NEW_CLUB_BOOK_AUTHORS,
                NEW_CLUB_PUBLICATION_YEAR,
                NEW_CLUB_DESCRIPTION,
                NEW_CLUB_TELEGRAM_CHAT_LINK);

        ClubModel createdClub = api.clubs.createClub(accessToken, clubData);

        step("Проверить, что данные созданного клуба совпадают с отправленными", () -> {
            assertThat(createdClub.id()).isNotNull();
            assertThat(createdClub.bookTitle()).isEqualTo(NEW_CLUB_BOOK_TITLE);
            assertThat(createdClub.bookAuthors()).isEqualTo(NEW_CLUB_BOOK_AUTHORS);
            assertThat(createdClub.publicationYear()).isEqualTo(NEW_CLUB_PUBLICATION_YEAR);
            assertThat(createdClub.description()).isEqualTo(NEW_CLUB_DESCRIPTION);
            assertThat(createdClub.telegramChatLink()).isEqualTo(NEW_CLUB_TELEGRAM_CHAT_LINK);
        });

        api.clubs.deleteClub(accessToken, createdClub.id());
    }

    @Test
    @DisplayName("Получение клуба по id")
    public void getClubByIdTest() {
        ClubBodyModel clubData = new ClubBodyModel(
                NEW_CLUB_BOOK_TITLE,
                NEW_CLUB_BOOK_AUTHORS,
                NEW_CLUB_PUBLICATION_YEAR,
                NEW_CLUB_DESCRIPTION,
                NEW_CLUB_TELEGRAM_CHAT_LINK);

        ClubModel createdClub = api.clubs.createClub(accessToken, clubData);

        ClubModel fetchedClub = api.clubs.getClubById(createdClub.id());

        step("Проверить, что полученные данные совпадают с созданным клубом", () -> {
            assertThat(fetchedClub.id()).isEqualTo(createdClub.id());
            assertThat(fetchedClub.bookTitle()).isEqualTo(NEW_CLUB_BOOK_TITLE);
            assertThat(fetchedClub.bookAuthors()).isEqualTo(NEW_CLUB_BOOK_AUTHORS);
            assertThat(fetchedClub.publicationYear()).isEqualTo(NEW_CLUB_PUBLICATION_YEAR);
            assertThat(fetchedClub.description()).isEqualTo(NEW_CLUB_DESCRIPTION);
            assertThat(fetchedClub.telegramChatLink()).isEqualTo(NEW_CLUB_TELEGRAM_CHAT_LINK);
        });

        api.clubs.deleteClub(accessToken, createdClub.id());
    }

    @Test
    @DisplayName("Обновление клуба")
    public void updateClubTest() {
        ClubBodyModel clubData = new ClubBodyModel(
                NEW_CLUB_BOOK_TITLE,
                NEW_CLUB_BOOK_AUTHORS,
                NEW_CLUB_PUBLICATION_YEAR,
                NEW_CLUB_DESCRIPTION,
                NEW_CLUB_TELEGRAM_CHAT_LINK);

        ClubModel createdClub = api.clubs.createClub(accessToken, clubData);

        ClubBodyModel updateData = new ClubBodyModel(
                UPDATED_CLUB_BOOK_TITLE,
                NEW_CLUB_BOOK_AUTHORS,
                NEW_CLUB_PUBLICATION_YEAR,
                UPDATED_CLUB_DESCRIPTION,
                NEW_CLUB_TELEGRAM_CHAT_LINK);

        ClubModel updatedClub = api.clubs.updateClubPut(accessToken, createdClub.id(), updateData);

        step("Проверить, что название книги и описание клуба обновились", () -> {
            assertThat(updatedClub.id()).isEqualTo(createdClub.id());
            assertThat(updatedClub.bookTitle()).isEqualTo(UPDATED_CLUB_BOOK_TITLE);
            assertThat(updatedClub.description()).isEqualTo(UPDATED_CLUB_DESCRIPTION);
        });

        api.clubs.deleteClub(accessToken, createdClub.id());
    }

    @Test
    @DisplayName("Удаление клуба")
    public void deleteClubTest() {
        ClubBodyModel clubData = new ClubBodyModel(
                NEW_CLUB_BOOK_TITLE,
                NEW_CLUB_BOOK_AUTHORS,
                NEW_CLUB_PUBLICATION_YEAR,
                NEW_CLUB_DESCRIPTION,
                NEW_CLUB_TELEGRAM_CHAT_LINK);

        ClubModel createdClub = api.clubs.createClub(accessToken, clubData);

        api.clubs.deleteClub(accessToken, createdClub.id());

        int statusCode = api.clubs.getClubStatusCode(createdClub.id());

        step("Проверить, что клуб больше не найден после удаления", () ->
                assertThat(statusCode).isEqualTo(CLUB_NOT_FOUND_STATUS_CODE));
    }
}
