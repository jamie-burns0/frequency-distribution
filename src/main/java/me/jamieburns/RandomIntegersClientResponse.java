package me.jamieburns;

import java.util.List;

public record RandomIntegersClientResponse(
    boolean success,
    List<Integer> data,
    String error
) {}
