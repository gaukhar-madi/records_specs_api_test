package specs.reviews;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetReviewSpec {

    public static ResponseSpecification successfulGetReviewResponseSpec =
            new ResponseSpecBuilder()
                    .log(ALL)
                    .expectStatusCode(200)
                    .expectBody(matchesJsonSchemaInClasspath(
                            "schemas/reviews/successful_get_review_response_schema.json"))
                    .expectBody("id", notNullValue())
                    .expectBody("user.id", notNullValue())
                    .build();

    public static ResponseSpecification successfulGetReviewsListResponseSpec =
            new ResponseSpecBuilder()
                    .log(ALL)
                    .expectStatusCode(200)
                    .expectBody(matchesJsonSchemaInClasspath(
                            "schemas/reviews/reviews_list_response_schema.json"))
                    .expectBody("count", greaterThanOrEqualTo(0))
                    .expectBody("results", notNullValue())
                    .build();

    public static ResponseSpecification getDeletedReviewsResponseSpec =
            new ResponseSpecBuilder()
                    .log(ALL)
                    .expectStatusCode(404)
                    .expectBody(matchesJsonSchemaInClasspath(
                            "schemas/reviews/get_deleted_reviews_schema.json"))
                    .expectBody("detail", notNullValue())
                    .build();
}
