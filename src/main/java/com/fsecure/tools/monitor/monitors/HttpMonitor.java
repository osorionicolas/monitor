package com.fsecure.tools.monitor.monitors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsecure.tools.monitor.client.HttpClient;
import com.fsecure.tools.monitor.config.HttpConfig;
import com.fsecure.tools.monitor.model.Url;
import com.fsecure.tools.monitor.model.UrlStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
@RequiredArgsConstructor
public class HttpMonitor {

    private final HttpConfig config;
    private final HttpClient client;
    private final RabbitTemplate rabbitTemplate;
    private List<UrlChecker> urlCheckers;

    @PostConstruct
    public void init() throws IOException {
        this.urlCheckers = new ObjectMapper()
                .readValue(new File(config.getMonitoredObjectsFilePath()), new TypeReference<List<Url>>() {
                })
                .stream().map(url -> new UrlChecker(url, client))
                .collect(toList());
    }

    @Scheduled(fixedDelayString = "${monitor.http.defaultCheckPeriodInMillis}")
    public void monitorUrls() {
        urlCheckers.parallelStream().forEach(urlChecker -> new Thread(() -> urlChecker.checkStatus(rabbitTemplate)).start());
    }

    public List<UrlStatus> urlsStatus() {
        return urlCheckers.parallelStream().map(UrlChecker::getUrlStatus).collect(toList());
    }
}
