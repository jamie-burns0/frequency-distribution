package me.jamieburns;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped 
public class RandomIntegersService {

    @RestClient
    RandomIntegersClient client;

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
