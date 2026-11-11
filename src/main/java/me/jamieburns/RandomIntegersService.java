package me.jamieburns;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.smallrye.mutiny.Uni;

public class RandomIntegersService {

    @RestClient
    RandomIntegersClient client;

    Uni<List<Integer>> generateRandomIntegers(int numberOfIntegers) {
        return client.generateRandomIntegers(numberOfIntegers)
            .onItem()
            .invoke(resp -> System.out.println("[resp=%s]".formatted(resp)))
            .replaceWith(List.of(10, 15, 4, 3));
    }
}
