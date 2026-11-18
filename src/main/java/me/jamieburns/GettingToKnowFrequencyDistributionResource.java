package me.jamieburns;

import java.util.List;
import java.util.function.Function;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Named;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/fd")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GettingToKnowFrequencyDistributionResource {

    private FrequencyDistributionService fdService;
    private RandomIntegersService randomService;
    private ApiResponseHelper helper;

    public GettingToKnowFrequencyDistributionResource(
        @Named(FrequencyDistributionService5.VERSION) FrequencyDistributionService fdService,
        RandomIntegersService randomService)
    {
        this.fdService = fdService;
        this.randomService = randomService;
        this.helper = new ApiResponseHelper(fdService);
    }

    @GET
    public Uni<Response> fd() {
        return getFrequencyDistribution(fdService::toFrequencyDistributionSortedByN);
    }

    @GET
    @Path("/sorted")
    public Uni<Response> fdSorted() {
        return getFrequencyDistribution(fdService::toFrequencyDistributionSortedByF);
    }

    private Uni<Response> getFrequencyDistribution(
        Function<List<Integer>, List<Frequency>> fn
    ) {
        return randomService.generateRandomIntegers()
            .map(response -> {

                if (!response.success()) {
                    return helper.newErrorResponse(response.error());
                }
                
                return helper.newOkResponse(fn.apply(response.data()));
            })
            .onFailure().recoverWithItem(throwable ->
                helper.newErrorResponse(
                    "Failed to call random integers service: %s".formatted(throwable.getMessage())
                )
            );
    }    
}
