package com.example.github_proxy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepoInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String description;
    private String cloneUrl;
    private Integer stars;
    private LocalDateTime createdAt;

    public void update(RepoInfo repoInfo) {
        this.fullName = repoInfo.getFullName();
        this.description = repoInfo.getDescription();
        this.cloneUrl = repoInfo.getCloneUrl();
        this.stars = repoInfo.getStars();
        this.createdAt = repoInfo.getCreatedAt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepoInfo repoInfo = (RepoInfo) o;
        return id != null && id.equals(repoInfo.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public static String generateFullName(String owner, String repoName) {
        return owner + "/" + repoName;
    }
}
