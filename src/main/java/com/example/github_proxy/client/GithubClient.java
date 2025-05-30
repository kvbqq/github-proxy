package com.example.github_proxy.client;

import com.example.github_proxy.model.RepoInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "githubClient", url = "https://api.github.com")
public interface GithubClient {

    @GetMapping("/repos/{owner}/{repo}")
    RepoInfo getRepoInfo(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo
    );
}
