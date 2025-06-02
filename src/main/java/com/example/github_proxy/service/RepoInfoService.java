package com.example.github_proxy.service;

import com.example.github_proxy.client.GithubClient;
import com.example.github_proxy.exception.RepositoryNotFoundException;
import com.example.github_proxy.model.RepoInfo;
import com.example.github_proxy.repository.RepoInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepoInfoService {
    private final GithubClient githubClient;
    private final RepoInfoRepository repoInfoRepository;

    public RepoInfo getRepoInfo(String owner, String repo) {
        return githubClient.getRepoInfo(owner, repo);
    }

    public RepoInfo saveRepoInfo(String owner, String repo) {
        RepoInfo repoInfo = githubClient.getRepoInfo(owner, repo);
        return repoInfoRepository.save(repoInfo);
    }

    public void deleteRepoInfo(String owner, String repo) {
        RepoInfo repoInfo = repoInfoRepository.findByFullName(owner + "/" + repo)
                .orElseThrow(() -> new RepositoryNotFoundException("Repository with given owner and repo name does not exist"));
        repoInfoRepository.delete(repoInfo);
    }
}
