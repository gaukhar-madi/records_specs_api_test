package models.reviews;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Ответ 400 POST /clubs/reviews/ — ошибки валидации обязательных полей.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AddWrongReviewResponseModel(
        List<String> club,
        List<String> review,
        List<String> assessment
) {}
