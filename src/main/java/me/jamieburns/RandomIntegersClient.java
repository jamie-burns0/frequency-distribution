package me.jamieburns;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.faulttolerance.api.ApplyGuard;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/generate")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey="random-integers")
public interface RandomIntegersClient {

    @GET
    // not configurable at runtime
    // @Retry(maxRetries = 2, delay = 3000)
    //
    // configurable at runtime
    @ApplyGuard("random-integers-client-retry")
    public Uni<RandomIntegersResponse> generateRandomIntegers(
        @QueryParam("numberOfIntegers") Integer numberOfIntegers
    );
}

