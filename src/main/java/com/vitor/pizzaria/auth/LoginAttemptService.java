package com.vitor.pizzaria.auth;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.util.Locale;

@Service
public class LoginAttemptService {

    private static final int MAX_FAILURES_PER_USERNAME_AND_IP = 5;
    private static final int MAX_FAILURES_PER_IP = 20;
    private static final int MAX_TRACKED_KEYS = 50_000;
    private static final Duration ATTEMPT_WINDOW = Duration.ofMinutes(15);

    private final Cache<String, Integer> attempts;

    public LoginAttemptService(Clock clock) {
        this.attempts = Caffeine.newBuilder()
                .expireAfterWrite(ATTEMPT_WINDOW)
                .maximumSize(MAX_TRACKED_KEYS)
                .ticker(() -> clock.instant().toEpochMilli() * 1_000_000)
                .build();
    }

    public boolean isBlocked(String username, String clientIp) {
        return count(ipKey(clientIp)) >= MAX_FAILURES_PER_IP
                || count(usernameAndIpKey(username, clientIp)) >= MAX_FAILURES_PER_USERNAME_AND_IP;
    }

    public void recordFailure(String username, String clientIp) {
        increment(ipKey(clientIp));
        increment(usernameAndIpKey(username, clientIp));
    }

    public void recordSuccess(String username, String clientIp) {
        attempts.invalidate(usernameAndIpKey(username, clientIp));
    }

    public void clear() {
        attempts.invalidateAll();
    }

    private int count(String key) {
        Integer currentCount = attempts.getIfPresent(key);
        return currentCount == null ? 0 : currentCount;
    }

    private void increment(String key) {
        attempts.asMap().merge(key, 1, Integer::sum);
    }

    private String ipKey(String clientIp) {
        return "ip:" + clientIp;
    }

    private String usernameAndIpKey(String username, String clientIp) {
        return "username-ip:" + username.toLowerCase(Locale.ROOT) + "|" + clientIp;
    }
}
