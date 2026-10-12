package tests;

public class TestData {

    public static final String LOGIN_USERNAME = "Gaukhar";
    public static final String LOGIN_PASSWORD = "123456";

    public static final String LOGIN_WRONG_USERNAME = "NonExistentUser123";
    public static final String LOGIN_WRONG_PASSWORD = "wrong_password";
    public static final String EMPTY_STRING = "";
    public static final String FIELD_REQUIRED_ERROR = "This field may not be blank.";
    public static final String FIELD_IS_REQUIRED_ERROR = "This field is required.";

    public static final String INVALID_REFRESH_TOKEN = "invalid_refresh_token_12345";
    public static final String INVALID_TOKEN_ERROR = "Token is invalid";

    public static final String LOGIN_TOKEN_PREFIX = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    public static final String REGISTRATION_IP_REGEXP =
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}"
                    + "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";

    public static final String UPDATED_FIRST_NAME = "Gaukhar_Updated";
    public static final String UPDATED_LAST_NAME = "QA_Engineer";
    public static final String UPDATED_EMAIL = "gaukhar_updated@example.com";
    public static final String INVALID_EMAIL = "invalid_email_format";

    public static final String INVALID_EMAIL_ERROR = "Enter a valid email address.";
    public static final String UNAUTHORIZED_ERROR = "Authentication credentials were not provided.";

    public static final String LOGIN_WRONG_CREDENTIALS_ERROR = "Invalid username or password.";
    public static final String REGISTRATION_EXISTING_USER_ERROR = "A user with that username already exists.";

    public static final String NEW_CLUB_BOOK_TITLE = "The Hitchhiker's Guide to the Galaxy";
    public static final String NEW_CLUB_BOOK_AUTHORS = "Douglas Adams";
    public static final Integer NEW_CLUB_PUBLICATION_YEAR = 1979;
    public static final String NEW_CLUB_DESCRIPTION = "Клуб любителей автостопом по галактике";
    public static final String NEW_CLUB_TELEGRAM_CHAT_LINK = "https://t.me/hitchhikers_guide";

    public static final String UPDATED_CLUB_BOOK_TITLE = "The Restaurant at the End of the Universe";
    public static final String UPDATED_CLUB_DESCRIPTION = "Обновлённое описание клуба";

    public static final int CLUB_NOT_FOUND_STATUS_CODE = 404;
}
