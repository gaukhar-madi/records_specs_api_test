package specs.reviews;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;

public class ReviewsSpec {

    public static RequestSpecification reviewsRequestSpec = baseRequestSpec;

    public static ResponseSpecification unauthorizedReviewResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/reviews/review_detail_error_response_schema.json"))
            .expectBody("detail", notNullValue())
            .build();

    public static ResponseSpecification forbiddenReviewResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(403)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/reviews/review_detail_error_response_schema.json"))
            .expectBody("detail", notNullValue())
            .build();
}
