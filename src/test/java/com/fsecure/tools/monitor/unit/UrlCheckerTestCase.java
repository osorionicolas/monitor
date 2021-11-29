package com.fsecure.tools.monitor.unit;

import com.fsecure.tools.monitor.client.HttpClient;
import com.fsecure.tools.monitor.config.AmqpConfig;
import com.fsecure.tools.monitor.model.Status;
import com.fsecure.tools.monitor.model.Url;
import com.fsecure.tools.monitor.model.UrlStatus;
import com.fsecure.tools.monitor.monitors.UrlChecker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Stream;

import static com.fsecure.tools.monitor.TestDataBuilder.*;
import static com.fsecure.tools.monitor.model.Status.ERROR;
import static com.fsecure.tools.monitor.model.Status.OK;
import static com.fsecure.tools.monitor.model.Status.WARNING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

public class UrlCheckerTestCase {

    private HttpClient httpClient = mock(HttpClient.class);
    private AmqpConfig amqpConfig = mock(AmqpConfig.class);
    private RestTemplate restTemplate = mock(RestTemplate.class);
    private RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);

    @ParameterizedTest
    @MethodSource("checkStatusTestArgs")
    public void checkStatus(Url url, ResponseEntity<String> response, Status status, String statusDescription) {
        UrlChecker urlChecker = new UrlChecker(url, httpClient, rabbitTemplate, amqpConfig);
        Boolean errorDescriptionFlag = statusDescription.equals(UNKHOST_ERROR_DESCRIPTION);
        when(httpClient.getConnection()).thenReturn(restTemplate);

        if (errorDescriptionFlag) {
            when(restTemplate.getForEntity(url.getUrl(), String.class)).thenThrow(new RuntimeException(statusDescription));
        } else {
            when(restTemplate.getForEntity(url.getUrl(), String.class)).thenReturn(response);
        }
        urlChecker.checkStatus();
        UrlStatus urlStatus = urlChecker.getUrlStatus();
        assertThat(urlStatus.getName(), is(url.getName()));
        assertThat(urlStatus.getLastStatus(), is(status));
        assertThat(urlStatus.getUrl(), is(url.getUrl()));
        assertThat(urlStatus.getLastResponseTime(), errorDescriptionFlag ? is(ZERO_LONG) : greaterThan(ZERO_LONG));
        assertThat(urlStatus.getLastCheckTime(), notNullValue());
        assertThat(urlStatus.getDescription(), containsString(statusDescription));
    }

    private static Stream<Arguments> checkStatusTestArgs() {
        return Stream.of(
                of(getOkUrlWithStringValidator(), getResponseOkUrlWithStringValidator(), OK, OK_DESCRIPTION),
                of(getOkUrlWithRegexValidator(), getResponseOkUrlWithRegexValidator(), OK, OK_DESCRIPTION),
                of(getWarningUrl(), getResponseWarningUrl(), WARNING, WARNING_DESCRIPTION),
                of(getErrorUrl404(), getResponseErrorUrl404(), ERROR, HTTP_ERROR_DESCRIPTION),
                of(getErrorUrlUnkHost(), getResponseErrorUrlUnkHost(), ERROR, UNKHOST_ERROR_DESCRIPTION)
        );
    }
}