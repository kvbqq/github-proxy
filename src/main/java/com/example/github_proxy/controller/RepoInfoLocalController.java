package com.example.github_proxy.controller;

import com.example.github_proxy.model.RepoInfoDto;
import com.example.github_proxy.service.RepoInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/local/repositories")
public class RepoInfoLocalController {
    private final RepoInfoService repoInfoService;

    @GetMapping("/{owner}/{repoName}")
    public RepoInfoDto getLocalRepoInfo(@PathVariable String owner, @PathVariable String repoName) {
        return repoInfoService.getLocalRepoInfo(owner, repoName);
    }
}
