package com.fsecure.tools.monitor.integration;

import com.fsecure.tools.monitor.model.UrlStatus;
import com.fsecure.tools.monitor.monitors.HttpMonitor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.fsecure.tools.monitor.TestDataBuilder.THREAD_SLEEP_TIME;
import static com.fsecure.tools.monitor.TestDataBuilder.URLS;
import static com.fsecure.tools.monitor.TestDataBuilder.URLS_STATUS;
import static com.fsecure.tools.monitor.TestDataBuilder.URLS_TO_CHECK_SIZE;
import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpMonitorTestCase extends AbstractTestCases {

    @Autowired
    private HttpMonitor monitor;

    @Test
    public void checkersAreLoadedOK() {
        List<UrlStatus> urlStatuses = monitor.urlsStatus();
        assertThat(urlStatuses.size(), is(URLS_TO_CHECK_SIZE));
        assertThat(urlStatuses.stream().map(url -> url.getUrl()).collect(toList()), is(URLS));
    }

    @Test
    public void schedulerIsOK() throws InterruptedException {
        sleep(THREAD_SLEEP_TIME);
        assertThat(monitor.urlsStatus().stream().map(status -> status.getLastStatus()).collect(toList()), is(URLS_STATUS));
    }
}
