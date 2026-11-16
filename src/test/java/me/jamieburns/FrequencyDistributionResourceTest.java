package me.jamieburns;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@QuarkusTest
class FrequencyDistributionResourceTest {

    @InjectMock
    RandomIntegersService mockService;

    @Test
    void testFdGetEndpoint() {
        
        when(mockService.generateRandomIntegers())
            .thenReturn(
                Uni.createFrom().item(
                    List.of(3, 1, 2)
                )
            );

        given()
            .when().get("/fd")
            .then()
            .statusCode(200)
            .body("frequencyList.size()", greaterThan(0))
            .body("frequencyList[0]", hasKey("n"))
            .body("frequencyList[0]", hasKey("f"));
    }

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

    @Test
    void testFdEndpointWithMockRandomService() {

        when(mockService.generateRandomIntegers())
            .thenReturn(
                Uni.createFrom()
                    .item(
                        List.of(3, 1, 2, 1, 3, 3, 4)
                    )
            );

        var fdService = new FrequencyDistributionService1();
        var helper = new FrequencyDistributionResourceHelper(fdService);

        FrequencyDistributionResource resource = new FrequencyDistributionResource(
                fdService,
                mockService,
                helper
        );

        Response response = resource.fd().await().indefinitely();

        assertEquals(200, response.getStatus());
        assertEquals(new FrequencyDistributionResponse(
            FrequencyDistributionService1.VERSION,
            List.of(
                new Frequency(1, 2),
                new Frequency(2, 1),
                new Frequency(3, 3),
                new Frequency(4, 1)
            )
        ), response.getEntity());
    }
}