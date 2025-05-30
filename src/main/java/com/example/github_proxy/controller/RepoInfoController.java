package com.example.github_proxy.controller;

import com.example.github_proxy.client.GithubClient;
import com.example.github_proxy.model.RepoInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/repositories")
public class RepoInfoController {
    private GithubClient githubClient;

    @GetMapping("/{owner}/{repo}")
    public RepoInfo getRepository(@PathVariable String owner, @PathVariable String repo) {
        return githubClient.getRepoInfo(owner, repo);
    }
}
