package test_base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.Attach;
import io.qameta.allure.Step;
import models.login.BookClubAuthModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.user.UpdateUserResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class UiTestBase extends TestBase {

    private static final String AUTH_STORAGE_KEY = "book_club_auth";

    @BeforeAll
    public static void setUpBrowser() {
        Configuration.baseUrl = "https://book-club.qa.guru";
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserSize = System.getProperty("browserSize", "1920x1080");
        Configuration.pageLoadStrategy = "eager";

        // запуск в Selenoid: ./gradlew test -DremoteUrl=https://user:pass@selenoid.qa.guru/wd/hub
        String remoteUrl = System.getProperty("remoteUrl");
        if (remoteUrl != null) {
            Configuration.remote = remoteUrl;
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                    "enableVNC", true,
                    "enableVideo", true
            ));
            Configuration.browserCapabilities = capabilities;
        }
    }

    @AfterEach
    public void addAttachments() {
        if (!WebDriverRunner.hasWebDriverStarted()) {
            return;
        }
        try {
            Attach.screenshotAs("Last screenshot");
            Attach.pageSource();
            Attach.browserConsoleLogs();
            if (Configuration.remote != null) {
                Attach.addVideo();
            }
        } finally {
            Selenide.closeWebDriver();
        }
    }

    /**
     * Авторизация без формы входа: токены и профиль берутся через API и кладутся в localStorage
     * в том же формате, в котором их сохраняет фронтенд. Возвращает access токен для API-запросов.
     */
    @Step("Авторизация через API под пользователем {username}")
    protected String loginViaApi(String username, String password) {
        SuccessfulLoginResponseModel tokens = api.auth.login(new LoginBodyModel(username, password));
        UpdateUserResponseModel user = api.users.getCurrentUser(tokens.access());
        BookClubAuthModel auth = new BookClubAuthModel(user, tokens.access(), tokens.refresh(), true);

        Selenide.open("/favicon.ico");
        Selenide.localStorage().setItem(AUTH_STORAGE_KEY, toJson(auth));

        return tokens.access();
    }

    private static String toJson(Object value) {
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
