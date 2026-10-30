package models.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import models.user.UpdateUserResponseModel;

/**
 * Значение ключа book_club_auth в localStorage, которое фронтенд сохраняет после входа.
 */
public record BookClubAuthModel(
        UpdateUserResponseModel user,
        String accessToken,
        String refreshToken,
        @JsonProperty("isAuthenticated") boolean isAuthenticated
) {}
