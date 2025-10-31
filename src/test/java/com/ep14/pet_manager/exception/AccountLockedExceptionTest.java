package com.ep14.pet_manager.exception;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountLockedExceptionTest {
    
    @Test
    void testGetLockTime() {
        LocalDateTime lockTime = LocalDateTime.now();
        AccountLockedException exception = new AccountLockedException("Account is locked", lockTime);
        
        assertThat(exception.getLockTime()).isEqualTo(lockTime);
    }
    
    @Test
    void testExceptionMessage() {
        String expectedMessage = "Account is locked";
        LocalDateTime lockTime = LocalDateTime.now();
        AccountLockedException exception = new AccountLockedException(expectedMessage, lockTime);
        
        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }
    
    @Test
    void testExceptionWithNullMessage() {
        LocalDateTime lockTime = LocalDateTime.now();
        AccountLockedException exception = new AccountLockedException(null, lockTime);
        
        assertThat(exception.getMessage()).isNull();
        assertThat(exception.getLockTime()).isEqualTo(lockTime);
    }
    
    @Test
    void testExceptionWithNullLockTime() {
        AccountLockedException exception = new AccountLockedException("Account is locked", null);
        
        assertThat(exception.getLockTime()).isNull();
        assertThat(exception.getMessage()).isEqualTo("Account is locked");
    }
    
    @Test
    void testExceptionIsRuntimeException() {
        LocalDateTime lockTime = LocalDateTime.now();
        AccountLockedException exception = new AccountLockedException("Account is locked", lockTime);
        
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}