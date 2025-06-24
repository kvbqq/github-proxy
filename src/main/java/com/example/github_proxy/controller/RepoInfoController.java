package com.example.github_proxy.controller;

import com.example.github_proxy.model.RepoInfoDto;
import com.example.github_proxy.service.RepoInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/repositories")
public class RepoInfoController {
    private final RepoInfoService repoInfoService;

    @GetMapping("/{owner}/{repoName}")
    public RepoInfoDto getRepoInfo(@PathVariable String owner, @PathVariable String repoName) {
        return repoInfoService.getRepoInfo(owner, repoName);
    }

    @PostMapping("/{owner}/{repoName}")
    public RepoInfoDto saveRepoInfo(@PathVariable String owner, @PathVariable String repoName) {
        return repoInfoService.saveRepoInfo(owner, repoName);
    }

    @PutMapping("/{owner}/{repoName}")
    public RepoInfoDto updateRepoInfo(@PathVariable String owner, @PathVariable String repoName) {
        return repoInfoService.updateRepoInfo(owner, repoName);
    }

    @DeleteMapping("/{owner}/{repoName}")
    public void deleteRepoInfo(@PathVariable String owner, @PathVariable String repoName) {
        repoInfoService.deleteRepoInfo(owner, repoName);
    }
}
