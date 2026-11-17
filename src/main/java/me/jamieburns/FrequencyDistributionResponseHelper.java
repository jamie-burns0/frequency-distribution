package me.jamieburns;

import java.util.List;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class FrequencyDistributionResponseHelper {

    private String version;

    public FrequencyDistributionResponseHelper(FrequencyDistributionService service) {
        this.version = service.version();
    }

    Response newEmptyResponse() {

        return newOkResponse(
            List.of()
        );
    }

    Response newOkResponse(List<Frequency> list) {

        return Response
            .ok()
            .type(MediaType.APPLICATION_JSON)
            .entity(
                new FrequencyDistributionResponse(version, list)
            )
            .build();
    }
}
