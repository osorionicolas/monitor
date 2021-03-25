package com.fsecure.tools.monitor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static com.fsecure.tools.monitor.utils.Constants.CONFIG_HTTP_PREFIX;


@Configuration
@ConfigurationProperties(prefix = CONFIG_HTTP_PREFIX)
@Getter @Setter
public class HttpConfig {

    private String defaultCheckPeriodInMillis;
    private String monitoredObjectsFilePath;
}
