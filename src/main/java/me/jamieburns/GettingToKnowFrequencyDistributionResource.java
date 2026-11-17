package me.jamieburns;

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
    private FrequencyDistributionResponseHelper helper;

    public GettingToKnowFrequencyDistributionResource(
        @Named(FrequencyDistributionService5.VERSION) FrequencyDistributionService fdService,
        RandomIntegersService randomService)
    {
        this.fdService = fdService;
        this.randomService = randomService;
        this.helper = new FrequencyDistributionResponseHelper(fdService);
    }

    @GET
    public Uni<Response> fd() {
        return randomService.generateRandomIntegers()
            .map(list -> 
                helper.newOkResponse(
                    fdService.toFrequencyDistributionSortedByN(list)
                )
            );
    }

    @GET
    @Path("/sorted")
    public Uni<Response> fdSorted() {
        return randomService.generateRandomIntegers()
            .map(list -> 
                helper.newOkResponse(
                    fdService.toFrequencyDistributionSortedByF(list)
                )
            );
    }
}
