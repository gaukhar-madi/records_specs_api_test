package models.reviews;

import models.clubs.ClubReviewModel;

import java.util.List;

/**
 * Ответ GET /clubs/reviews/ — пагинированный список отзывов.
 */
public record ReviewsListResponseModel(
        Integer count,
        String next,
        String previous,
        List<ClubReviewModel> results
) {}
