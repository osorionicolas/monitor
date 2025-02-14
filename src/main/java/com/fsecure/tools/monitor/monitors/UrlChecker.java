package com.fsecure.tools.monitor.monitors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsecure.tools.monitor.client.HttpClient;
import com.fsecure.tools.monitor.config.AmqpConfig;
import com.fsecure.tools.monitor.model.MailBean;
import com.fsecure.tools.monitor.model.Status;
import com.fsecure.tools.monitor.model.Url;
import com.fsecure.tools.monitor.model.UrlStatus;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.util.regex.Pattern;

import static com.fsecure.tools.monitor.model.Status.ERROR;
import static com.fsecure.tools.monitor.model.Status.OK;
import static com.fsecure.tools.monitor.model.Status.WARNING;
import static com.fsecure.tools.monitor.utils.Constants.HEADER_RESPONSE_TIME_IN_MS;
import static com.fsecure.tools.monitor.utils.Constants.ZERO_RESPONSE_TIME;
import static java.lang.Long.valueOf;
import static java.lang.String.format;
import static java.util.Calendar.getInstance;
import static org.slf4j.LoggerFactory.getLogger;

@Slf4j
public class UrlChecker {

    private static final Logger logger = getLogger(UrlChecker.class);

    private final HttpClient client;
    private final String patternValidator;
    private final UrlStatus urlStatus;
    private final RabbitTemplate rabbitTemplate;
    private final AmqpConfig amqpConfig;

    public UrlChecker(Url url, HttpClient client, RabbitTemplate rabbitTemplate, AmqpConfig amqpConfig) {
        this.patternValidator = url.getRegexValidator();
        this.urlStatus = new UrlStatus(url.getName(), url.getUrl());
        this.client = client;
        this.rabbitTemplate = rabbitTemplate;
        this.amqpConfig = amqpConfig;
    }

    public void checkStatus() {
        try {
            logger.debug("Check status for {} with url: {}", urlStatus.getName(), urlStatus.getUrl());
            ResponseEntity<String> response = client.getConnection().getForEntity(urlStatus.getUrl(), String.class);
            Boolean is2xx = response.getStatusCode().is2xxSuccessful();
            Status currentStatus = is2xx && hasContentRequirement(response.getBody()) ? OK : is2xx ? WARNING : ERROR;
            updateUrlStatus(currentStatus,
                    currentStatus.equals(OK) ? "OK" :
                            currentStatus.equals(WARNING) ? format("Content does not match validator: %s", patternValidator) :
                                    format("code: %d - %s", response.getStatusCodeValue(), response.getBody()),
                    getResponseTime(response.getHeaders()));
        } catch (HttpClientErrorException | HttpServerErrorException | UnknownHttpStatusCodeException exception) {
            MailBean mail = new MailBean("Monitor: HttpClientErrorException | HttpServerErrorException | UnknownHttpStatusCodeException", exception.getResponseBodyAsString(), amqpConfig.getDestinationAddrs());
            this.sendAmqpMessage(mail);
            updateUrlStatus(ERROR, exception.getResponseBodyAsString(), getResponseTime(exception.getResponseHeaders()));
        } catch (Exception unknownException) {
            MailBean mail = new MailBean("Monitor: unknownException", unknownException.getMessage(), amqpConfig.getDestinationAddrs());
            this.sendAmqpMessage(mail);
            updateUrlStatus(ERROR, unknownException.getMessage(), ZERO_RESPONSE_TIME);
        }
    }

    private void sendAmqpMessage(MailBean mail){
        try {
            rabbitTemplate.convertAndSend(amqpConfig.getExchange(), amqpConfig.getKey(), new ObjectMapper().writeValueAsString(mail));
        }
        catch(JsonProcessingException e){
            log.error("RabbitMQ message couldn't be sent");
        }
    }

    private boolean hasContentRequirement(String body) {
        return Pattern.compile(patternValidator).matcher(body).find();
    }

    private synchronized void updateUrlStatus(Status status, String description, Long responseTime) {
        String lastCheckTime = getInstance().getTime().toString();
        urlStatus.setLastStatus(status);
        urlStatus.setDescription(description);
        urlStatus.setLastCheckTime(lastCheckTime);
        urlStatus.setLastResponseTime(responseTime);
        logger.info("NAME: {} | URL: {} | STATUS: {} | RESPONSE_TIME: {}ms | CHECK_TIME: {} | DESCRIPTION: {}", urlStatus.getName(), urlStatus.getUrl(), urlStatus.getLastStatus().toString(), urlStatus.getLastResponseTime(), urlStatus.getLastCheckTime(), urlStatus.getDescription());
    }

    private Long getResponseTime(HttpHeaders headers) {
        return valueOf(headers.get(HEADER_RESPONSE_TIME_IN_MS).get(0));
    }

    public synchronized UrlStatus getUrlStatus() {
        return urlStatus;
    }
}
