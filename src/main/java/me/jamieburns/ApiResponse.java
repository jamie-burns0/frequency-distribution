package me.jamieburns;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record ApiResponse<T>(boolean success, String algorithmVersion, T frequencyDistribution, String error) {
    
    public static <T> ApiResponse<T> ok(String algorithmVersion, T frequencyDistribution) {
        return new ApiResponse<>(true, algorithmVersion, frequencyDistribution, null);
    }
    
    public static <T> ApiResponse<T> error(String errorMessage) {
        return new ApiResponse<>(false, null, null, errorMessage);
    }
}
