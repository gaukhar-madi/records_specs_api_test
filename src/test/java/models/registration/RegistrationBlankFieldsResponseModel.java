package models.registration;

import java.util.List;

public record RegistrationBlankFieldsResponseModel(List<String> username, List<String> password) {}
