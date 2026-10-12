package models.clubs;

/**
 * Тело запроса для создания/обновления клуба POST, PUT /clubs/.
 */
public record ClubBodyModel(
        String bookTitle,
        String bookAuthors,
        Integer publicationYear,
        String description,
        String telegramChatLink
) {}
