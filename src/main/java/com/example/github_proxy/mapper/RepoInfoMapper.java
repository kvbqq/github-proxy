package com.example.github_proxy.mapper;

import com.example.github_proxy.model.RepoInfo;
import com.example.github_proxy.model.RepoInfoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RepoInfoMapper {
    RepoInfoDto toDto(RepoInfo repoInfo);
}
