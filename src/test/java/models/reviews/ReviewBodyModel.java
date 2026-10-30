package models.reviews;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Тело запроса для создания/обновления отзыва POST, PUT, PATCH /clubs/reviews/.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReviewBodyModel(
        Integer club,
        String review,
        Integer assessment,
        Integer readPages
) {}
