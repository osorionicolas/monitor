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
import static java.lang.String.valueOf;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class TestDataBuilder {

    public static final String MONITOR_HTTP_DEFAULTCHECKPERIOD = "20000";
    public static final String MONITOR_HTTP_FILE_END_PATH = "/src/test/resources/urls.json";
    public static final Integer URLS_TO_CHECK_SIZE = 5;
    public static final Integer SCHEDULED_SLEEP_TIME = 30000;
    public static final Long LAST_RESPONSE_TIME = 500L;
    public static final Long ZERO_LONG = 0L;
    public static final Integer URLS_TO_MONITOR = 5;
    public static final String WRONG_JSON_PATH = "fake.json";
    public static List<String> URLS = Lists.newArrayList("https://en.wikipedia.org/wiki/Argentina", "https://en.wikipedia.org/wiki/Finland", "https://en.wikipedia.org/wiki/F-Secure", "http://localhost:8080/fake_page", "https://google.com.fake");
    public static List<Status> URLS_STATUS = Lists.newArrayList(OK, OK, WARNING, ERROR, ERROR);

    public static final String OK_DESCRIPTION = "OK";
    public static final String WARNING_DESCRIPTION = "Content does not match validator:";
    public static final String HTTP_ERROR_DESCRIPTION = "Not Found";
    public static final String UNKHOST_ERROR_DESCRIPTION = "I/O error on GET";

    public static final String urlsJsonFilePath() {
        return TestDataBuilder.class.getClassLoader().getResource("urls.json").getPath();
    }

    public static final List<Url> getUrls() throws IOException {
        return new ObjectMapper().readValue(TestDataBuilder.class.getClassLoader().getResourceAsStream("urls.json"), new TypeReference<List<Url>>() {
        });
    }

    public static final Url getOkUrlWithStringValidator() {
        return getUrl("WikiArgentina", "https://en.wikipedia.org/wiki/Argentina", "Buenos Aires");
    }

    public static final ResponseEntity<String> getResponseOkUrlWithStringValidator() {
        return new ResponseEntity<>("Argentina - Buenos Aires", getDefaultHeader(), HttpStatus.OK);
    }

    public static final Url getOkUrlWithRegexValidator() {
        return getUrl("WikiFinland", "https://en.wikipedia.org/wiki/Finland", "(Arg\\w music | tango)");
    }

    public static final ResponseEntity<String> getResponseOkUrlWithRegexValidator() {
        return new ResponseEntity<>("Argentinian music is based on tango", getDefaultHeader(), HttpStatus.OK);
    }

    public static final Url getWarningUrl() {
        return getUrl("F-Secure", "https://en.wikipedia.org/wiki/F-Secure", "Ezequiel is hired");
    }

    public static final ResponseEntity<String> getResponseWarningUrl() {
        return new ResponseEntity<>("F-Secure is the best company in Finland", getDefaultHeader(), HttpStatus.OK);
    }

    public static final Url getErrorUrl404() {
        return getUrl("NotFound", "http://localhost:8080/fake_page", "content");
    }

    public static final ResponseEntity<String> getResponseErrorUrl404() {
        return new ResponseEntity<>("Not Found", getDefaultHeader(), NOT_FOUND);
    }

    public static final Url getErrorUrlUnkHost() {
        return getUrl("UnknownHost", "https://google.com.fake", "content to verify");
    }

    public static final ResponseEntity<String> getResponseErrorUrlUnkHost() {
        return new ResponseEntity<>("Not Found", getDefaultHeader(), INTERNAL_SERVER_ERROR);
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
        header.set(HEADER_RESPONSE_TIME_IN_MS, valueOf(LAST_RESPONSE_TIME));
        return header;
    }
}
