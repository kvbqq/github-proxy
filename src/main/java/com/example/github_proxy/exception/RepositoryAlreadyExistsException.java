package com.example.github_proxy.exception;

import org.springframework.http.HttpStatus;

public class RepositoryAlreadyExistsException extends GithubProxyException {
    public RepositoryAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
