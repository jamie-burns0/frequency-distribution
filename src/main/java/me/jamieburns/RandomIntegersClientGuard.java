package me.jamieburns;

import java.time.temporal.ChronoUnit;

import io.smallrye.common.annotation.Identifier;
import io.smallrye.faulttolerance.api.Guard;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped 
public class RandomIntegersClientGuard {

    @Produces
    @Identifier("random-integers-client-retry")
    public static final Guard RETRY_GUARD = Guard.create()
        .withRetry()
            // maxRetries is overridden at runtime through property
            // quarkus.fault-tolerance."random-integers-client-retry".retry.max-retries
            // in application.properties or on command line
            .maxRetries(2)
            .delay(3000, ChronoUnit.MILLIS)
            .done()
        .build();
}
