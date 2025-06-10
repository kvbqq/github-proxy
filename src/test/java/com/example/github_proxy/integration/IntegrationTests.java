package com.example.github_proxy.integration;

import com.example.github_proxy.model.RepoInfo;
import com.example.github_proxy.model.RepoInfoDto;
import com.example.github_proxy.model.RepoInfoGithubResponse;
import com.example.github_proxy.repository.RepoInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 8089)
@Sql(scripts = {"/init.sql"}, executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = {"/clean.sql"}, executionPhase = AFTER_TEST_METHOD)
public class IntegrationTests {

    @Autowired
    WireMockServer wireMockServer;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RepoInfoRepository repoInfoRepository;

    @LocalServerPort
    int appPort;

    @Test
    void getRepoInfo_dataCorrect_shouldReturnRepoInfo() throws JsonProcessingException {
        String owner = "kvbqq";
        String repoName = "medical-clinic";

        RepoInfoGithubResponse repoInfo = RepoInfoGithubResponse.builder()
                .fullName("kvbqq/medical-clinic")
                .description("desc")
                .build();

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/repos/kvbqq/medical-clinic"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(repoInfo))
                ));

        String url = String.format("http://localhost:%s/repositories/%s/%s", appPort, owner, repoName);
        ResponseEntity<RepoInfoDto> response = restTemplate.getForEntity(url, RepoInfoDto.class);

        assertAll(
                () -> assertEquals("kvbqq/medical-clinic", response.getBody().getFullName()),
                () -> assertEquals("desc", response.getBody().getDescription())
        );
    }

    @Test
    void getLocalRepoInfo_dataCorrect_shouldReturnRepoInfo() {
        String owner = "kvbqq";
        String repoName = "test-repo";

        String url = String.format("http://localhost:%s/local/repositories/%s/%s", appPort, owner, repoName);
        ResponseEntity<RepoInfo> response = restTemplate.getForEntity(url, RepoInfo.class);

        assertAll(
                () -> assertEquals("kvbqq/test-repo", response.getBody().getFullName()),
                () -> assertEquals("desc", response.getBody().getDescription())
        );
    }

    @Test
    void saveRepoInfo_dataCorrect_shouldSaveRepoInfo() throws JsonProcessingException {
        String owner = "kvbqq";
        String repoName = "medical-clinic";

        RepoInfoGithubResponse repoInfoGithubResponse = RepoInfoGithubResponse.builder()
                .fullName("kvbqq/medical-clinic")
                .description("desc")
                .build();

        RepoInfo repoInfo = RepoInfo.builder()
                .fullName("kvbqq/medical-clinic")
                .description("desc")
                .build();

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/repos/kvbqq/medical-clinic"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(repoInfoGithubResponse))
                ));

        String url = String.format("http://localhost:%s/repositories/%s/%s", appPort, owner, repoName);
        ResponseEntity<RepoInfo> response = restTemplate.postForEntity(url, repoInfo, RepoInfo.class);

        assertAll(
                () -> assertEquals("kvbqq/medical-clinic", response.getBody().getFullName()),
                () -> assertEquals("desc", response.getBody().getDescription())
        );
    }

    @Test
    void updateRepoInfo_dataCorrect_shouldUpdateRepoInfo() throws JsonProcessingException {
        String owner = "kvbqq";
        String repoName = "test-repo";

        RepoInfoGithubResponse repoInfoGithubResponse = RepoInfoGithubResponse.builder()
                .fullName("kvbqq/test-repo")
                .description("desc")
                .build();

        RepoInfo repoInfo = RepoInfo.builder()
                .fullName("kvbqq/test-repo")
                .description("desc")
                .build();

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/repos/kvbqq/test-repo"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(repoInfoGithubResponse))
                ));

        String url = String.format("http://localhost:%s/repositories/%s/%s", appPort, owner, repoName);
        ResponseEntity<RepoInfo> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(repoInfo), RepoInfo.class);

        assertAll(
                () -> assertEquals("kvbqq/test-repo", response.getBody().getFullName()),
                () -> assertEquals("desc", response.getBody().getDescription())
        );
    }

    @Test
    void deleteRepoInfo_dataCorrect_shouldDeleteRepoInfo() {
        String owner = "kvbqq";
        String repoName = "delete-repo";

        String url = String.format("http://localhost:%s/repositories/%s/%s", appPort, owner, repoName);
        restTemplate.delete(url);

        assertEquals(Optional.empty(), repoInfoRepository.findByFullName("kvbqq/delete-repo"));
    }
}
