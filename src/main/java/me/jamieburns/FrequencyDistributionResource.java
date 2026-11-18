package me.jamieburns;

import jakarta.inject.Named;
import jakarta.ws.rs.Consumes;
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
    private ApiResponseHelper helper;

    public FrequencyDistributionResource(
        @Named(FrequencyDistributionService5.VERSION) FrequencyDistributionService fdService)
    {
        this.fdService = fdService;
        this.helper = new ApiResponseHelper(fdService);
    }

    @POST
    public Response fd(FrequencyDistributionRequest request) {

        if( requestHasNoData(request) )
        {
            return helper.newEmptyResponse();
        }

        return helper.newOkResponse(
            fdService.toFrequencyDistributionSortedByN(request.data())
        );
    }

    @POST 
    @Path("/sorted")
    public Response fdSorted(FrequencyDistributionRequest request) {

        if( requestHasNoData(request) )
        {
            return helper.newEmptyResponse();
        }

        return helper.newOkResponse(
            fdService.toFrequencyDistributionSortedByF(request.data())
        );
    }

    public boolean requestHasNoData(FrequencyDistributionRequest request) {
        return 
            request == null ||
            request.data() == null ||
            request.data().isEmpty();
    }
}
