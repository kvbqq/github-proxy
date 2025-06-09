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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 8089)
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
        String repoName = "medical-clinic";

        RepoInfo repoInfo = RepoInfo.builder()
                .fullName("kvbqq/medical-clinic")
                .description("desc")
                .build();

        repoInfoRepository.save(repoInfo);

        String url = String.format("http://localhost:%s/local/repositories/%s/%s", appPort, owner, repoName);
        ResponseEntity<RepoInfo> response = restTemplate.getForEntity(url, RepoInfo.class);

        assertAll(
                () -> assertEquals("kvbqq/medical-clinic", response.getBody().getFullName()),
                () -> assertEquals("desc", response.getBody().getDescription())
        );
    }
}
