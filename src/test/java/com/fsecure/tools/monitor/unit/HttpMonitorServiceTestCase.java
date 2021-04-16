package com.fsecure.tools.monitor.unit;

import com.fsecure.tools.monitor.model.UrlStatus;
import com.fsecure.tools.monitor.monitors.HttpMonitor;
import com.fsecure.tools.monitor.service.HttpMonitorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.annotation.Resource;
import java.io.IOException;

import static com.fsecure.tools.monitor.TestDataBuilder.URLS_TO_CHECK_SIZE;
import static com.fsecure.tools.monitor.TestDataBuilder.getUrls;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class HttpMonitorServiceTestCase {

    @Mock
    private HttpMonitor monitor;

    @InjectMocks
    @Resource
    private HttpMonitorServiceImpl service;

    @BeforeEach
    public void setUp() throws IOException {
        openMocks(this);
        when(monitor.urlsStatus()).thenReturn(getUrls().stream().map(url -> new UrlStatus(url.getName(), url.getUrl())).collect(toList()));
    }

    @Test
    public void getStatusOk() {
        assertThat(service.getStatus().size(), is(URLS_TO_CHECK_SIZE));
    }

}
