package com.fsecure.tools.monitor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsecure.tools.monitor.model.Status;
import com.fsecure.tools.monitor.model.Url;
import com.google.common.collect.Lists;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import static com.fsecure.tools.monitor.model.Status.ERROR;
import static com.fsecure.tools.monitor.model.Status.OK;
import static com.fsecure.tools.monitor.model.Status.WARNING;
import static com.fsecure.tools.monitor.utils.Constants.HEADER_RESPONSE_TIME_IN_MS;
import static java.lang.Math.floor;
import static java.lang.Math.random;
import static java.lang.String.valueOf;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class TestDataBuilder {

    public static final String MONITOR_HTTP_DEFAULTCHECKPERIOD = "20000";
    public static final String MONITOR_HTTP_FILE_END_PATH = "/src/test/resources/urls.json";
    public static final Integer URLS_TO_CHECK_SIZE = 5;
    public static final Integer SCHEDULED_SLEEP_TIME = 20000;
    public static final Integer THREAD_SLEEP_TIME = 2000;
    public static final Long ZERO_LONG = 0L;
    public static final String WRONG_JSON_PATH = "fake.json";
    public static List<String> URLS = Lists.newArrayList("http://localhost:1080/wiki/Argentina", "http://localhost:1080/wiki/Finland", "http://localhost:1080/wiki/F-Secure", "http://localhost:1080/fake_page", "http://fake:1080/unkhost");
    public static List<Status> URLS_STATUS = Lists.newArrayList(OK, OK, WARNING, ERROR, ERROR);
    public static final String MODEL_ATTR = "urlsStatus";
    public static final String VIEW_NAME = "httpMonitor/Dashboard";
    public static final String OK_DESCRIPTION = "OK";
    public static final String WARNING_DESCRIPTION = "Content does not match validator:";
    public static final String HTTP_ERROR_DESCRIPTION = "Not Found";
    public static final String UNKHOST_ERROR_DESCRIPTION = "I/O error on GET";
    public static final String AMQP_EXCHANGE = "mail-exchange";
    public static final String AMQP_KEY = "mail.received";
    public static final String AMQP_DEST_ADDRS = "test@test.com";

    public static final String urlsJsonFilePath() {
        return TestDataBuilder.class.getClassLoader().getResource("urls.json").getPath();
    }

    public static final List<Url> getUrls() throws IOException {
        return new ObjectMapper().readValue(TestDataBuilder.class.getClassLoader().getResourceAsStream("urls.json"), new TypeReference<List<Url>>() {
        });
    }

    public static final String OK_URL_WITH_STRING_VALIDATOR_PATH = "/wiki/Argentina";
    public static final String OK_URL_WITH_STRING_VALIDATOR_BODY_RESPONSE = "Argentina - Buenos Aires";

    public static final String OK_URL_WITH_REGEX_VALIDATOR_PATH = "/wiki/Finland";
    public static final String OK_URL_WITH_REGEX_VALIDATOR_BODY_RESPONSE = "Argentinian music is based on tango";

    public static final String WARNING_URL_PATH = "/wiki/F-Secure";
    public static final String WARNING_URL_BODY_RESPONSE= "F-Secure is the best company in Finland";

    public static final String ERROR_URL_404_PATH = "/fake_page";
    public static final String ERROR_URL_404_BODY_RESPONSE = "Not Found";

    public static final String ERROR_URL_UNKHOST_PATH = "/unkhost";
    public static final String ERROR_URL_UNKHOST_BODY_RESPONSE = "Not Found host";


    public static final Url getOkUrlWithStringValidator() {
        return getUrl("WikiArgentina", "https://en.wikipedia.org" + OK_URL_WITH_STRING_VALIDATOR_PATH, "Buenos Aires");
    }

    public static final ResponseEntity<String> getResponseOkUrlWithStringValidator() {
        return new ResponseEntity<>(OK_URL_WITH_STRING_VALIDATOR_BODY_RESPONSE, getDefaultHeader(), HttpStatus.OK);
    }

    public static final Url getOkUrlWithRegexValidator() {
        return getUrl("WikiFinland", "https://en.wikipedia.org" + OK_URL_WITH_REGEX_VALIDATOR_PATH, "(Arg\\w music | tango)");
    }

    public static final ResponseEntity<String> getResponseOkUrlWithRegexValidator() {
        return new ResponseEntity<>(OK_URL_WITH_REGEX_VALIDATOR_BODY_RESPONSE, getDefaultHeader(), HttpStatus.OK);
    }

    public static final Url getWarningUrl() {
        return getUrl("F-Secure", "https://en.wikipedia.org" + WARNING_URL_PATH, "Ezequiel is hired");
    }

    public static final ResponseEntity<String> getResponseWarningUrl() {
        return new ResponseEntity<>(WARNING_URL_BODY_RESPONSE, getDefaultHeader(), HttpStatus.OK);
    }

    public static final Url getErrorUrl404() {
        return getUrl("NotFound", "http://localhost:8080" + ERROR_URL_404_PATH, "content");
    }

    public static final ResponseEntity<String> getResponseErrorUrl404() {
        return new ResponseEntity<>(ERROR_URL_404_BODY_RESPONSE, getDefaultHeader(), NOT_FOUND);
    }

    public static final Url getErrorUrlUnkHost() {
        return getUrl("UnknownHost", "https://google.com.fake" + ERROR_URL_UNKHOST_PATH, "content to verify");
    }

    public static final ResponseEntity<String> getResponseErrorUrlUnkHost() {
        return new ResponseEntity<>(ERROR_URL_UNKHOST_BODY_RESPONSE, getDefaultHeader(), INTERNAL_SERVER_ERROR);
    }

    public static final Integer getRandomResponseTime(){
        return (int) floor(random()*(200-10+1)+200);
    }

    private static Url getUrl(String name, String url_path, String regexValidator) {
        Url url = new Url();
        url.setName(name);
        url.setUrl(url_path);
        url.setRegexValidator(regexValidator);
        return url;
    }

    private static HttpHeaders getDefaultHeader() {
        HttpHeaders header = new HttpHeaders();
        header.set(HEADER_RESPONSE_TIME_IN_MS, valueOf(getRandomResponseTime()));
        return header;
    }
}
