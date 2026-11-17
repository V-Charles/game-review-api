package com.gamereview.gamereview_api.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {

    private static final int MAX_REQUESTS_PER_MINUTE = 5;

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String clientKey) {
        return buckets.computeIfAbsent(clientKey, this::createNewBucket);
    }

    private Bucket createNewBucket(String clientKey) {
        Bandwidth limit = Bandwidth.builder()
                .capacity(MAX_REQUESTS_PER_MINUTE)
                .refillIntervally(MAX_REQUESTS_PER_MINUTE, Duration.ofMinutes(1))
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}