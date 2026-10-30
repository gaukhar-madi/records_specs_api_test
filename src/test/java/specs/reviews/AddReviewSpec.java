package specs.reviews;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class AddReviewSpec {

    public static ResponseSpecification successfulAddReviewsSpec =
            new ResponseSpecBuilder()
                    .log(ALL)
                    .expectStatusCode(201)
                    .expectBody(matchesJsonSchemaInClasspath(
                            "schemas/reviews/successful_add_reviews_response_schema.json"))
                    .expectBody("id", notNullValue())
                    .expectBody("club", notNullValue())
                    .expectBody("user.id", notNullValue())
                    .expectBody("user.username", notNullValue())
                    .expectBody("review", notNullValue())
                    .expectBody("assessment", notNullValue())
                    .expectBody("readPages", notNullValue())
                    .expectBody("created", notNullValue())
                    .expectBody("modified", nullValue())
                    .build();

    public static ResponseSpecification addWrongReviewsSpec =
            new ResponseSpecBuilder()
                    .log(ALL)
                    .expectStatusCode(400)
                    .expectBody(matchesJsonSchemaInClasspath(
                            "schemas/reviews/add_wrong_reviews_schema.json"))
                    .expectBody("club", notNullValue())
                    .expectBody("review", notNullValue())
                    .expectBody("assessment", notNullValue())
                    .build();
}