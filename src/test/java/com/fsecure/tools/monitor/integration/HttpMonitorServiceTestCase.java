package com.fsecure.tools.monitor.integration;

import com.fsecure.tools.monitor.service.HttpMonitorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.fsecure.tools.monitor.TestDataBuilder.SCHEDULED_SLEEP_TIME;
import static com.fsecure.tools.monitor.TestDataBuilder.URLS_STATUS;
import static java.lang.Thread.sleep;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class HttpMonitorServiceTestCase extends AbstractTestCases{

    @Autowired
    private HttpMonitorService service;

    @Test
    public void getStatus() throws InterruptedException {
        sleep(SCHEDULED_SLEEP_TIME);
        assertThat(service.getStatus().stream().map(status -> status.getLastStatus()).collect(toList()), is(URLS_STATUS));
    }
}
