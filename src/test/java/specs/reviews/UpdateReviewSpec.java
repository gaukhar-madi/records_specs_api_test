package specs.reviews;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class UpdateReviewSpec {

    public static ResponseSpecification successfulUpdatePutReviewsSpec =
            new ResponseSpecBuilder()
                    .log(ALL)
                    .expectStatusCode(200)
                    .expectBody(matchesJsonSchemaInClasspath(
                            "schemas/reviews/successful_update_reviews_response.json"))
                    .expectBody("id", notNullValue())
                    .expectBody("club", notNullValue())
                    .expectBody("user.id", notNullValue())
                    .expectBody("user.username", notNullValue())
                    .expectBody("review", notNullValue())
                    .expectBody("assessment", notNullValue())
                    .expectBody("readPages", notNullValue())
                    .expectBody("created", notNullValue())
                    .expectBody("modified", notNullValue())
                    .build();
}