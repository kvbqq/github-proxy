package com.example.github_proxy.controller;

import com.example.github_proxy.mapper.RepoInfoMapper;
import com.example.github_proxy.model.RepoInfoDto;
import com.example.github_proxy.service.RepoInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/repositories")
public class RepoInfoController {
    private final RepoInfoService repoInfoService;
    private final RepoInfoMapper repoInfoMapper;

    @GetMapping("/{owner}/{repo}")
    public RepoInfoDto getRepoInfo(@PathVariable String owner, @PathVariable String repo) {
        return repoInfoMapper.toDto(repoInfoService.getRepoInfo(owner, repo));
    }

    @PostMapping("/{owner}/{repo}")
    public RepoInfoDto saveRepoInfo(@PathVariable String owner, @PathVariable String repo) {
        return repoInfoMapper.toDto(repoInfoService.saveRepoInfo(owner, repo));
    }

    @DeleteMapping("/{owner}/{repo}")
    public void deleteRepoInfo(@PathVariable String owner, @PathVariable String repo) {
        repoInfoService.deleteRepoInfo(owner, repo);
    }
}
