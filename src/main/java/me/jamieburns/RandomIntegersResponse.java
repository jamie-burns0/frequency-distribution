package me.jamieburns;

import java.util.List;

public record RandomIntegersResponse(
    boolean success,
    List<Integer> data,
    String error
) {}
