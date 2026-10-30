package specs.reviews;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;

public class DeleteReviewSpec {

    public static ResponseSpecification successfulDeleteReviewSpec =
            new ResponseSpecBuilder()
                    .log(ALL)
                    .expectStatusCode(204)
                    .build();
}