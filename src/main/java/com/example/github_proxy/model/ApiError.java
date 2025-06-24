package com.example.github_proxy.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@JsonPropertyOrder({"status", "error", "message", "timestamp"})
public class ApiError {
    private Integer status;
    private String error;
    private String message;
    private ZonedDateTime timestamp;

    public ApiError(HttpStatus errorStatus, String message) {
        this.status = errorStatus.value();
        this.error = errorStatus.getReasonPhrase();
        this.message = message;
        this.timestamp = ZonedDateTime.now();
    }
}
