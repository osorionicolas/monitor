package com.fsecure.tools.monitor.monitors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsecure.tools.monitor.client.HttpClient;
import com.fsecure.tools.monitor.config.HttpConfig;
import com.fsecure.tools.monitor.model.Url;
import com.fsecure.tools.monitor.model.UrlStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Configuration
@EnableScheduling
public class HttpMonitor {

    @Autowired
    private HttpConfig config;
    @Autowired
    private HttpClient client;
    private List<UrlChecker> urlCheckers;

    @PostConstruct
    public void init() throws IOException {
        this.urlCheckers = new ObjectMapper()
                .readValue(new File(config.getMonitoredObjectsFilePath()), new TypeReference<List<Url>>() {})
                .stream().map(url -> new UrlChecker(url, client))
                .collect(toList());
    }

    @Scheduled(fixedDelayString = "${monitor.http.defaultCheckPeriodInMillis}")
    public void monitorUrls() {
        urlCheckers.stream().forEach(urlChecker -> urlChecker.checkStatus());
    }

    public List<UrlStatus> urlsStatus(){
        return urlCheckers.stream().map(url -> url.getUrlStatus()).collect(toList());
    }
}
