package com.fsecure.tools.monitor.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockserver.integration.ClientAndServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.fsecure.tools.monitor.TestDataBuilder.ERROR_URL_404_BODY_RESPONSE;
import static com.fsecure.tools.monitor.TestDataBuilder.ERROR_URL_404_PATH;
import static com.fsecure.tools.monitor.TestDataBuilder.OK_URL_WITH_REGEX_VALIDATOR_BODY_RESPONSE;
import static com.fsecure.tools.monitor.TestDataBuilder.OK_URL_WITH_REGEX_VALIDATOR_PATH;
import static com.fsecure.tools.monitor.TestDataBuilder.OK_URL_WITH_STRING_VALIDATOR_BODY_RESPONSE;
import static com.fsecure.tools.monitor.TestDataBuilder.OK_URL_WITH_STRING_VALIDATOR_PATH;
import static com.fsecure.tools.monitor.TestDataBuilder.WARNING_URL_BODY_RESPONSE;
import static com.fsecure.tools.monitor.TestDataBuilder.WARNING_URL_PATH;
import static com.fsecure.tools.monitor.TestDataBuilder.getRandomResponseTime;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.unlimited;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest
@TestPropertySource(locations = "/mock.properties")
@AutoConfigureMockMvc
public abstract class AbstractTestCases {

    private static ClientAndServer mockServer;

    @BeforeAll
    public static void startServer() {
        mockServer = startClientAndServer(1080);
        setHttpResponsesToMockServer(mockServer, OK_URL_WITH_STRING_VALIDATOR_PATH, OK.value(), OK_URL_WITH_STRING_VALIDATOR_BODY_RESPONSE);
        setHttpResponsesToMockServer(mockServer, OK_URL_WITH_REGEX_VALIDATOR_PATH, OK.value(), OK_URL_WITH_REGEX_VALIDATOR_BODY_RESPONSE);
        setHttpResponsesToMockServer(mockServer, WARNING_URL_PATH, OK.value(), WARNING_URL_BODY_RESPONSE);
        setHttpResponsesToMockServer(mockServer, ERROR_URL_404_PATH, NOT_FOUND.value(), ERROR_URL_404_BODY_RESPONSE);
    }

    @AfterAll
    public static void stopServer() {
        mockServer.stop();
    }

    private static void setHttpResponsesToMockServer(ClientAndServer mockServer, String path, Integer statusCodeResponse, String responseContent) {
        mockServer
                .when(
                        request().withMethod(GET.name()).withPath(path), unlimited()
                )
                .respond(
                        response()
                                .withStatusCode(statusCodeResponse)
                                .withHeader("\"Content-type\", \"plain/text\"")
                                .withBody(responseContent)
                                .withDelay(MILLISECONDS, getRandomResponseTime())
                );
    }
}
