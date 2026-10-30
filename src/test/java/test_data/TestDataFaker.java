package test_data;

import models.clubs.ClubBodyModel;
import models.reviews.ReviewBodyModel;
import net.datafaker.Faker;
import java.util.Locale;
import java.util.UUID;

public class TestDataFaker {
    Faker faker = new Faker();
    Faker fakerRu = new Faker(new Locale("ru"));

    public String
            fakerUserFirstName = faker.name().firstName() + faker.random().nextInt(9999),
            fakerUserTwoFirstName = faker.name().firstName() + faker.random().nextInt(9999),
            fakerInvalideName = faker.name().fullName(),
            fakerPassword = String.valueOf(faker.random().nextInt(9999999)),
            // bookTitle у клубов уникален, а на общем стенде тысячи клубов — суффикс до 99 давал совпадения
            fakerBookTitle = faker.book().title() + " " + UUID.randomUUID().toString().substring(0, 8),
            fakerbookAuthors = faker.book().author(),
            fakerDescription = faker.chuckNorris().fact(),
            fakerTelegramChatLink = "https://t.me/" + faker.name().firstName();

    public Integer
            fakerPublicationYear = faker.random().nextInt(1800, 2026),
            fakerAssessment = faker.random().nextInt(1, 5),
            fakerReadPages = faker.random().nextInt(20, 70);

    public ClubBodyModel clubBody() {
        return new ClubBodyModel(fakerBookTitle, fakerbookAuthors, fakerPublicationYear,
                fakerDescription, fakerTelegramChatLink);
    }

    public ReviewBodyModel reviewBody(Integer clubId) {
        return new ReviewBodyModel(clubId, fakerDescription, fakerAssessment, fakerReadPages);
    }
}
