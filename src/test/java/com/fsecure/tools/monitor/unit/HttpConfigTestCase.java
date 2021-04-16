package com.fsecure.tools.monitor.unit;

import com.fsecure.tools.monitor.config.HttpConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.fsecure.tools.monitor.TestDataBuilder.MONITOR_HTTP_DEFAULTCHECKPERIOD;
import static com.fsecure.tools.monitor.TestDataBuilder.MONITOR_HTTP_FILE_END_PATH;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = HttpConfig.class)
@TestPropertySource("classpath:application.properties")
public class HttpConfigTestCase {

    @Autowired
    private HttpConfig config;

    @Test
    public void httpConfigOk() {
        assertThat(config.getDefaultCheckPeriodInMillis(), is(MONITOR_HTTP_DEFAULTCHECKPERIOD));
        assertThat(config.getMonitoredObjectsFilePath(), endsWith(MONITOR_HTTP_FILE_END_PATH));
    }
}
