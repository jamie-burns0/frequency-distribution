package me.jamieburns;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped 
public class RandomIntegersService {

    @RestClient
    RandomIntegersClient client;

    final int numberOfIntegers;

    public RandomIntegersService(
        @ConfigProperty(name = "max-number-of-integers")
        int numberOfIntegers
    )
    {
        this.numberOfIntegers = numberOfIntegers;
    }

    Uni<List<Integer>> generateRandomIntegers() {
        return generateRandomIntegers(numberOfIntegers);
    }

    Uni<List<Integer>> generateRandomIntegers(int numberOfIntegers) {
        return client.generateRandomIntegers(numberOfIntegers)
            .onItem().invoke(this::ensureSuccess)
            .onItem().transform(RandomIntegersClientResponse::data);
    }

    private void ensureSuccess(RandomIntegersClientResponse resp) {
        if(!resp.success()) {
            throw new RuntimeException("Call to random-integers client failed. [error=%s]".formatted(resp.error()));
        }
    }
}
