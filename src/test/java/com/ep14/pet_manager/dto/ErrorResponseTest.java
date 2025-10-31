package com.ep14.pet_manager.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import java.time.temporal.ChronoUnit;

public class ErrorResponseTest {
    
    @Test
    void testGetErrorCode() {
        ErrorResponse errorResponse = new ErrorResponse("Error message", "ERR_001");
        
        assertThat(errorResponse.getErrorCode()).isEqualTo("ERR_001");
    }

    @Test
    void testGetMessage() {
        ErrorResponse errorResponse = new ErrorResponse("Error message", "ERR_001");
        
        assertThat(errorResponse.getMessage()).isEqualTo("Error message");
    }

    @Test
    void testGetTimestamp() {
        LocalDateTime before = LocalDateTime.now();
        ErrorResponse errorResponse = new ErrorResponse("Error message", "ERR_001");
        LocalDateTime after = LocalDateTime.now();
        
        assertThat(errorResponse.getTimestamp()).isBetween(before, after);
    }

    @Test
    void testSetErrorCode() {
        ErrorResponse errorResponse = new ErrorResponse("Error message", "ERR_001");
        errorResponse.setErrorCode("ERR_002");
        
        assertThat(errorResponse.getErrorCode()).isEqualTo("ERR_002");
    }

    @Test
    void testSetMessage() {
        ErrorResponse errorResponse = new ErrorResponse("Error message", "ERR_001");
        errorResponse.setMessage("New error message");
        
        assertThat(errorResponse.getMessage()).isEqualTo("New error message");
    }

    @Test
    void testSetTimestamp() {
        ErrorResponse errorResponse = new ErrorResponse("Error message", "ERR_001");
        LocalDateTime newTimestamp = LocalDateTime.of(2024, 1, 1, 12, 0);
        errorResponse.setTimestamp(newTimestamp);
        
        assertThat(errorResponse.getTimestamp()).isEqualTo(newTimestamp);
    }
    
    @Test
    void testConstructorWithNullMessage() {
        ErrorResponse errorResponse = new ErrorResponse(null, "ERR_001");
        
        assertThat(errorResponse.getMessage()).isNull();
        assertThat(errorResponse.getErrorCode()).isEqualTo("ERR_001");
        assertThat(errorResponse.getTimestamp()).isNotNull();
    }
    
    @Test
    void testConstructorWithNullErrorCode() {
        ErrorResponse errorResponse = new ErrorResponse("Error message", null);
        
        assertThat(errorResponse.getMessage()).isEqualTo("Error message");
        assertThat(errorResponse.getErrorCode()).isNull();
        assertThat(errorResponse.getTimestamp()).isNotNull();
    }
    
    @Test
    void testConstructorTimestampIsClose() {
        LocalDateTime expectedTime = LocalDateTime.now();
        ErrorResponse errorResponse = new ErrorResponse("Error message", "ERR_001");
        
        assertThat(errorResponse.getTimestamp()).isCloseTo(expectedTime, within(1, ChronoUnit.SECONDS));
    }
    
    @Test
    void testSetNullValues() {
        ErrorResponse errorResponse = new ErrorResponse("Error message", "ERR_001");
        errorResponse.setMessage(null);
        errorResponse.setErrorCode(null);
        errorResponse.setTimestamp(null);
        
        assertThat(errorResponse.getMessage()).isNull();
        assertThat(errorResponse.getErrorCode()).isNull();
        assertThat(errorResponse.getTimestamp()).isNull();
    }
    
    @Test
    void testConstructorWithEmptyStrings() {
        ErrorResponse errorResponse = new ErrorResponse("", "");
        
        assertThat(errorResponse.getMessage()).isEmpty();
        assertThat(errorResponse.getErrorCode()).isEmpty();
        assertThat(errorResponse.getTimestamp()).isNotNull();
    }
}