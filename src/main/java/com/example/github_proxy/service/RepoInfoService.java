package com.example.github_proxy.service;

import com.example.github_proxy.client.GithubClient;
import com.example.github_proxy.exception.RepositoryAlreadyExistsException;
import com.example.github_proxy.exception.RepositoryNotFoundException;
import com.example.github_proxy.mapper.RepoInfoMapper;
import com.example.github_proxy.model.RepoInfo;
import com.example.github_proxy.model.RepoInfoDto;
import com.example.github_proxy.repository.RepoInfoRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepoInfoService {
    private final GithubClient githubClient;
    private final RepoInfoRepository repoInfoRepository;
    private final RepoInfoMapper repoInfoMapper;

    public RepoInfoDto getRepoInfo(String owner, String repoName) {
        try {
            return repoInfoMapper.toDto(githubClient.getRepoInfo(owner, repoName));
        } catch (FeignException.NotFound ex) {
            throw new RepositoryNotFoundException("Requested repository could not be found");
        }
    }

    public RepoInfoDto getLocalRepoInfo(String owner, String repoName) {
        return repoInfoMapper.toDto(repoInfoRepository.findByFullName(RepoInfo.generateFullName(owner, repoName))
                .orElseThrow(() -> new RepositoryNotFoundException("Requested repository could not be found in database")));
    }

    public RepoInfoDto saveRepoInfo(String owner, String repoName) {
        if (repoInfoRepository.findByFullName(RepoInfo.generateFullName(owner, repoName)).isPresent()) {
            throw new RepositoryAlreadyExistsException("Requested repository already exists in database");
        }
        try {
            RepoInfo repoInfo = repoInfoMapper.toEntity(githubClient.getRepoInfo(owner, repoName));
            return repoInfoMapper.toDto(repoInfoRepository.save(repoInfo));
        } catch (FeignException.NotFound ex) {
            throw new RepositoryNotFoundException("Requested repository could not be found");
        }

    }

    public RepoInfoDto updateRepoInfo(String owner, String repoName) {
        RepoInfo existingRepoInfo = repoInfoRepository.findByFullName(RepoInfo.generateFullName(owner, repoName))
                .orElseThrow(() -> new RepositoryNotFoundException("Requested repository could not be found in database"));
        try {
            RepoInfo repoInfo = repoInfoMapper.toEntity(githubClient.getRepoInfo(owner, repoName));
            existingRepoInfo.update(repoInfo);
            return repoInfoMapper.toDto(repoInfoRepository.save(existingRepoInfo));
        } catch (FeignException.NotFound ex) {
            throw new RepositoryNotFoundException("Requested repository could not be found");
        }

    }

    public void deleteRepoInfo(String owner, String repoName) {
        RepoInfo repoInfo = repoInfoRepository.findByFullName(RepoInfo.generateFullName(owner, repoName))
                .orElseThrow(() -> new RepositoryNotFoundException("Requested repository could not be found in database"));
        repoInfoRepository.delete(repoInfo);
    }
}
