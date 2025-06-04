package com.example.github_proxy.exception;

import com.example.github_proxy.model.ApiError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GithubProxyExceptionHandler {
    @ExceptionHandler(GithubProxyException.class)
    public ResponseEntity<ApiError> handleGithubProxyException(GithubProxyException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ApiError(ex.getStatus(), ex.getMessage()));
    }
}
