package me.jamieburns;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class FrequencyDistributionResourceTest {

    @Test
    void testFdPostEndpointWithData() {

        given()
            .contentType("application/json")
            .body("{\"data\": [3, 1, 2, 1, 3, 3, 4]}")
            .when().post("/fd")
            .then()
            .statusCode(200)
            .body("frequencyList.size()", equalTo(4));
    }

    @Test
    void testFdPostEndpointWithEmptyData() {

        given()
            .contentType("application/json")
            .body("{\"data\": []}")
            .when().post("/fd")
            .then()
            .statusCode(200)
            .body("frequencyList.size()", equalTo(0))
            .body("version", notNullValue());
    }

    static Stream<FrequencyDistributionService> frequencyDistributionServices() {
        return Stream.of(
            new FrequencyDistributionService1(),
            new FrequencyDistributionService2(),
            new FrequencyDistributionService3(),
            new FrequencyDistributionService4(),
            new FrequencyDistributionService5()
        );
    }

    @ParameterizedTest
    @MethodSource("frequencyDistributionServices")
    void testFdEndpointWithFixedList(FrequencyDistributionService service) {
        List<Integer> input = List.of(3, 1, 2, 1, 3, 3, 4);
        List<Frequency> expected = List.of(
                new Frequency(1, 2),
                new Frequency(2, 1),
                new Frequency(3, 3),
                new Frequency(4, 1)
        );

        assertEquals(expected, service.toFrequencyDistributionSortedByN(input));
    }

    @ParameterizedTest
    @MethodSource("frequencyDistributionServices")
    void testSortedFdEndpointWithFixedList(FrequencyDistributionService service) {
        List<Integer> input = List.of(3, 1, 2, 1, 1, 3, 3, 4, 4);
        List<Frequency> expected = List.of(
            new Frequency(1, 3),
            new Frequency(3, 3),
            new Frequency(4, 2),
            new Frequency(2, 1)
        );

        assertEquals(expected, service.toFrequencyDistributionSortedByF(input));
    }

    static Stream<FrequencyDistributionRequest> requestsWithNoData() {
        return Stream.of(
            null,
            new FrequencyDistributionRequest(null),
            new FrequencyDistributionRequest(List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("requestsWithNoData")
    void testRequestHasNoData(FrequencyDistributionRequest request) {
        var resource = new FrequencyDistributionResource(
            new FrequencyDistributionService5()
        );
        
        assertEquals(true, resource.requestHasNoData(request));
    }

    static Stream<FrequencyDistributionRequest> requestsWithData() {
        return Stream.of(
            new FrequencyDistributionRequest(List.of(1, 2, 3)),
            new FrequencyDistributionRequest(List.of(42))
        );
    }

    @ParameterizedTest
    @MethodSource("requestsWithData")
    void testRequestHasData(FrequencyDistributionRequest request) {
        var resource = new FrequencyDistributionResource(
            new FrequencyDistributionService5()
        );
        
        assertEquals(false, resource.requestHasNoData(request));
    }
}