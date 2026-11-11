package me.jamieburns;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/generate")
@RegisterRestClient(configKey="random-integers")
public interface RandomIntegersClient {

    @GET
    public Uni<Object> generateRandomIntegers(@QueryParam("numberOfIntegers") Integer numberOfIntegers);
}
