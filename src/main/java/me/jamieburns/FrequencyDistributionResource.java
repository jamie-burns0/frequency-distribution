package me.jamieburns;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Named;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/fd")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FrequencyDistributionResource {

    private FrequencyDistributionService fdService;
    private RandomIntegersService randomService;
    private FrequencyDistributionResourceHelper helper;

    public FrequencyDistributionResource(
        @Named(FrequencyDistributionService5.VERSION) FrequencyDistributionService fdService,
        RandomIntegersService randomService,
        FrequencyDistributionResourceHelper helper)
    {
        this.fdService = fdService;
        this.randomService = randomService;
        this.helper = helper;
    }

    @GET
    public Uni<Response> fd() {
        return randomService.generateRandomIntegers()
            .invoke(x -> System.out.println("x=%s".formatted(x)))
            .map(list -> Response
                .ok()
                .entity(new FrequencyDistributionResponse(
                    fdService.version(),
                    fdService.toFrequencyDistributionSortedByN(list)
                ))
                .build()
            );
    }

    @POST
    public Response fd(FrequencyDistributionRequest request) {

        if( request == null || 
            request.data() == null || 
            request.data().isEmpty() 
        ) {
            return helper.emptyResponse();
        }

        return Response
            .ok()
            .entity(new FrequencyDistributionResponse(
                fdService.version(),
                fdService.toFrequencyDistributionSortedByN(request.data())
            ))
            .build();
    }

    @GET
    @Path("/sorted")
    public Uni<Response> fdSorted() {
        return randomService.generateRandomIntegers()
            .map(list -> Response
                .ok()
                .entity(new FrequencyDistributionResponse(
                    fdService.version(),
                    fdService.toFrequencyDistributionSortedByF(list)
                ))
                .build()
            );
    }

    @POST 
    @Path("/sorted")
    public Response fdSorted(FrequencyDistributionRequest request) {

        if( request == null || 
            request.data() == null || 
            request.data().isEmpty() 
        ) {
            return helper.emptyResponse();
        }

        return Response
            .ok()
            .entity(
                fdService.toFrequencyDistributionSortedByF(request.data())
            )
            .build();
    }
}
