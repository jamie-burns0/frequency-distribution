package me.jamieburns;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;

import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@QuarkusTest
class GettingToKnowFrequencyDistributionResourceTest {

    @InjectMock
    RandomIntegersService mockService;

    @Test
    void testFdGetEndpoint() {
        when(mockService.generateRandomIntegers())
            .thenReturn(
                Uni.createFrom().item(List.of(3, 1, 2))
            );

        given()
            .when().get("/fd")
            .then()
            .statusCode(200)
            .body("frequencyList.size()", greaterThan(0))
            .body("frequencyList[0]", notNullValue());
    }

    @Test
    void testFdSortedGetEndpoint() {
        when(mockService.generateRandomIntegers())
            .thenReturn(
                Uni.createFrom().item(List.of(3, 1, 2, 1, 3, 3, 4))
            );

        given()
            .when().get("/fd/sorted")
            .then()
            .statusCode(200)
            .body("frequencyList.size()", greaterThan(0))
            .body("version", notNullValue());
    }
}
