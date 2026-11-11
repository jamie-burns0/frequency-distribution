package me.jamieburns;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Named;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/fd")
public class FrequencyDistributionResource {

    @RegisterForReflection
    public record FrequencyListResponse(String version, List<Frequency> frequencies) {}

    private FrequencyDistributionService fdService;
    private RandomIntegersService randomService;

    public FrequencyDistributionResource(
        @Named(FrequencyDistributionService5.VERSION) FrequencyDistributionService fdService,
        RandomIntegersService randomService)
    {
        this.fdService = fdService;
        this.randomService = randomService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> fd() {
        return randomService.generateRandomIntegers(10)
            .map(list -> Response
                .ok()
                .entity(new FrequencyListResponse(
                    fdService.version(),
                    fdService.toFrequencyDistributionSortedByN(list)
                ))
                .build()
            );
    }

    @GET
    @Path("/sorted")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> fdSorted() {
        return randomService.generateRandomIntegers(10)
            .map(list -> Response
                .ok()
                .entity(new FrequencyListResponse(
                    fdService.version(),
                    fdService.toFrequencyDistributionSortedByF(list)
                ))
                .build()
            );
    }
}
