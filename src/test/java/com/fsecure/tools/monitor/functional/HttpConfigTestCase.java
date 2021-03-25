package com.fsecure.tools.monitor.functional;

import com.fsecure.tools.monitor.AbstractTestCases;
import com.fsecure.tools.monitor.config.HttpConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.fsecure.tools.monitor.TestDataBuilder.MONITOR_HTTP_DEFAULTCHECKPERIOD;
import static com.fsecure.tools.monitor.TestDataBuilder.MONITOR_HTTP_FILE_FORMAT;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpConfigTestCase extends AbstractTestCases {

    @Autowired
    private HttpConfig config;

    @Test
    public void httpConfigLoadedOK(){
        assertThat(config.getDefaultCheckPeriodInMillis(), is(MONITOR_HTTP_DEFAULTCHECKPERIOD));
        assertThat(config.getMonitoredObjectsFilePath(), endsWith(MONITOR_HTTP_FILE_FORMAT));
    }
}
