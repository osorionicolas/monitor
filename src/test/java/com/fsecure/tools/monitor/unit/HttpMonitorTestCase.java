package com.fsecure.tools.monitor.unit;

import com.fsecure.tools.monitor.TestDataBuilder;
import com.fsecure.tools.monitor.client.HttpClient;
import com.fsecure.tools.monitor.config.HttpConfig;
import com.fsecure.tools.monitor.model.Url;
import com.fsecure.tools.monitor.model.UrlStatus;
import com.fsecure.tools.monitor.monitors.HttpMonitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

import static com.fsecure.tools.monitor.TestDataBuilder.SCHEDULED_SLEEP_TIME;
import static com.fsecure.tools.monitor.TestDataBuilder.WRONG_JSON_PATH;
import static com.fsecure.tools.monitor.TestDataBuilder.getUrls;
import static java.lang.Boolean.FALSE;
import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class HttpMonitorTestCase {

    @Mock
    private HttpConfig config;

    @Mock
    private HttpClient client;

    @InjectMocks
    @Resource
    private HttpMonitor monitor;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        when(config.getMonitoredObjectsFilePath()).thenReturn(TestDataBuilder.urlsJsonFilePath());
        when(client.getConnection()).thenReturn(mock(RestTemplate.class));
    }

    @Test
    public void initOk() throws IOException {
        monitor.init();
        List<UrlStatus> urlStatusesFromMonitor = monitor.urlsStatus();
        List<Url> urlStatusesFromDataBuilder = getUrls();
        assertThat(urlStatusesFromMonitor.size(), is(urlStatusesFromDataBuilder.size()));
        assertThat(urlStatusesFromMonitor.stream().map(url -> url.getName()).collect(toList()),
                is(urlStatusesFromDataBuilder.stream().map(url -> url.getName()).collect(toList())));
    }

    @Test
    public void initFail() {
        when(config.getMonitoredObjectsFilePath()).thenReturn(WRONG_JSON_PATH);
        Exception exception = assertThrows(IOException.class, () -> monitor.init());
        assertThat(exception.getMessage(), startsWith(WRONG_JSON_PATH));
    }

    @Test
    public void monitorUrls() throws IOException, InterruptedException {
        monitor.init();
        monitor.monitorUrls();
        sleep(SCHEDULED_SLEEP_TIME);
        assertTrue(monitor.urlsStatus().stream().map(status -> status.getLastCheckTime().isEmpty()).allMatch(bol -> bol.equals(FALSE)));
    }
}
