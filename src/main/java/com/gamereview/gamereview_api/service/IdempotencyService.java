package com.gamereview.gamereview_api.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IdempotencyService {

    private final Set<String> processedKeys = ConcurrentHashMap.newKeySet();

    public boolean isProcessed(String key) {
        return processedKeys.contains(key);
    }

    public void markAsProcessed(String key) {
        processedKeys.add(key);
    }
}