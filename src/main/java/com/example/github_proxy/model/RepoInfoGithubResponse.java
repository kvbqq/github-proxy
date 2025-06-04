package com.example.github_proxy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RepoInfoGithubResponse {
    @JsonProperty("full_name")
    private String fullName;
    private String description;
    @JsonProperty("clone_url")
    private String cloneUrl;
    @JsonProperty("stargazers_count")
    private Integer stars;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
