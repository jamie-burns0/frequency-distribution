package me.jamieburns;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection 
public record FrequencyDistributionResponse(
    String version, 
    List<Frequency> frequencyList
) {}
