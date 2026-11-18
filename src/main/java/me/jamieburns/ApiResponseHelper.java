package me.jamieburns;

import java.util.List;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class ApiResponseHelper {

    private String version;

    public ApiResponseHelper(FrequencyDistributionService service) {
        this.version = service.version();
    }

    Response newEmptyResponse() {
        return newOkResponse(List.of());
    }

    Response newOkResponse(List<Frequency> list) {
        return Response
            .ok()
            .type(MediaType.APPLICATION_JSON)
            .entity(
                ApiResponse.ok(version, list)
            )
            .build();
    }

    Response newErrorResponse(String errorMessage) {
        return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR)
            .type(MediaType.APPLICATION_JSON)
            .entity(
                ApiResponse.error(errorMessage)
            )
            .build();
    }
}
