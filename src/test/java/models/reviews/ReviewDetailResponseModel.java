package models.reviews;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Ответ с ошибкой 401, 403, 404 для /clubs/reviews/.
 * При невалидном токене сервер дополнительно возвращает code и messages — они не нужны.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ReviewDetailResponseModel(String detail) {}
