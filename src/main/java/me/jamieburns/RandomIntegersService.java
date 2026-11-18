package me.jamieburns;

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

    Uni<RandomIntegersResponse> generateRandomIntegers() {
        return generateRandomIntegers(numberOfIntegers);
    }

    Uni<RandomIntegersResponse> generateRandomIntegers(int numberOfIntegers) {
        return client.generateRandomIntegers(numberOfIntegers);
    }
}
