package com.ep14.pet_manager.exception;

import java.time.LocalDateTime;

public class AccountLockedException extends RuntimeException{

    private final LocalDateTime lockTime;

    public AccountLockedException(String message, LocalDateTime lockTime) {
        super(message);
        this.lockTime = lockTime;
    }

    public LocalDateTime getLockTime() {
        return lockTime;
    }
}
