package me.jamieburns;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class FrequencyDistributionResourceHelper {

    private FrequencyDistributionService service;

    public FrequencyDistributionResourceHelper(
        @Named(FrequencyDistributionService5.VERSION) FrequencyDistributionService service
    ) {
        this.service = service;
    }

    Response emptyResponse() {

        return Response
            .ok()
            .type(MediaType.APPLICATION_JSON)
            .entity(
                new FrequencyDistributionResponse(
                    service.version(),
                    service.emptyFrequencyDistribution()
                )
            )
            .build();
    }
}
