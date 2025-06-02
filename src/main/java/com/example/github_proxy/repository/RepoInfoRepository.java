package com.example.github_proxy.repository;

import com.example.github_proxy.model.RepoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoInfoRepository extends JpaRepository<RepoInfo, Long> {
    Optional<RepoInfo> findByFullName(String fullName);
}
