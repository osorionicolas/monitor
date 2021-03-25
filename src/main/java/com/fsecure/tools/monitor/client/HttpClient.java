package com.fsecure.tools.monitor.client;

import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Component
@Getter
public class HttpClient {

    private static final Logger logger = getLogger(HttpClient.class);
    private RestTemplate connection;

    @PostConstruct
    public void init() {
        logger.debug("Initializing HTTP client");
        this.connection = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new ResponseTimeInterceptor());
        connection.setInterceptors(interceptors);
        logger.debug("HTTP client initialization done");
    }
}
