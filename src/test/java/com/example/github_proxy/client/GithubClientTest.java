package com.example.github_proxy.client;

import com.example.github_proxy.model.RepoInfoGithubResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@SpringBootTest
@AutoConfigureWireMock(port = 8089)
public class GithubClientTest {

    @Autowired
    GithubClient githubClient;

    @Autowired
    WireMockServer wireMockServer;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        wireMockServer.start();
    }

    @AfterEach
    void shutdown() {
        wireMockServer.stop();
    }

    @Test
    void shouldGetRepositoryFromApi() throws JsonProcessingException {
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

        var result = githubClient.getRepoInfo("kvbqq", "medical-clinic");

        assertAll(
                () -> assertEquals("kvbqq/medical-clinic", result.getFullName()),
                () -> assertEquals("desc", result.getDescription())
        );
    }
}
