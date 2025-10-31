package com.ep14.pet_manager.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3;
    private static final int LOCK_TIME_MINUTES = 15;

    private final Map<String, AttemptInfo> attemptsCache = new ConcurrentHashMap<>();

    public void loginSucceeded(String email) {
        attemptsCache.remove(email);
    }

    public void loginFailed(String email) {
        AttemptInfo info = attemptsCache.getOrDefault(email, new AttemptInfo());
        info.incrementAttempts();

        if (info.getAttempts() >= MAX_ATTEMPTS) {
            info.setLockTime(LocalDateTime.now().plusMinutes(LOCK_TIME_MINUTES));
        }

        attemptsCache.put(email, info);
    }

    public boolean isBlocked(String email) {
        AttemptInfo info = attemptsCache.get(email);

        if (info == null) {
            return false;
        }

        if (info.getLockTime() != null && LocalDateTime.now().isAfter(info.getLockTime())) {
            attemptsCache.remove(email);
            return false;
        }

        return info.getAttempts() >= MAX_ATTEMPTS;
    }

    public int getRemainingAttempts(String email) {
        AttemptInfo info = attemptsCache.get(email);
        if (info == null) {
            return MAX_ATTEMPTS;
        }
        return Math.max(0, MAX_ATTEMPTS - info.getAttempts());
    }

    public LocalDateTime getLockTime(String email) {
        AttemptInfo info = attemptsCache.get(email);
        return info != null ? info.getLockTime() : null;
    }

    private static class AttemptInfo {
        private int attempts = 0;
        private LocalDateTime lockTime;

        public void incrementAttempts() {
            attempts++;
        }

        public int getAttempts() {
            return attempts;
        }

        public LocalDateTime getLockTime() {
            return lockTime;
        }

        public void setLockTime(LocalDateTime lockTime) {
            this.lockTime = lockTime;
        }
    }
}
