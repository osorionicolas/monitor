package com.fsecure.tools.monitor.functional;

import com.fsecure.tools.monitor.AbstractTestCases;
import com.fsecure.tools.monitor.client.HttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import static com.fsecure.tools.monitor.TestDataBuilder.HTTP_404_URL;
import static com.fsecure.tools.monitor.TestDataBuilder.UNKNOWN_HOST_ERROR_MSG;
import static com.fsecure.tools.monitor.TestDataBuilder.UNKNOWN_HOST_URL;
import static com.fsecure.tools.monitor.TestDataBuilder.WIKI_ARG_URL;
import static com.fsecure.tools.monitor.utils.Constants.HEADER_RESPONSE_TIME_IN_MS;
import static java.lang.Long.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResponseTimeInterceptorTestCase extends AbstractTestCases {

    @Autowired
    private HttpClient client;

    @Test
    public void interceptorOk(){
        ResponseEntity<String> response = client.getConnection().getForEntity(WIKI_ARG_URL, String.class);
        assertThat(valueOf(response.getHeaders().get(HEADER_RESPONSE_TIME_IN_MS).get(0)), greaterThan(0L));
    }

    @Test
    public void interceptorOkOn404(){
        try{
            client.getConnection().getForEntity(HTTP_404_URL, String.class);
        } catch (HttpClientErrorException exp){
            assertThat(valueOf(exp.getResponseHeaders().get(HEADER_RESPONSE_TIME_IN_MS).get(0)), greaterThan(0L));
        }
    }

    @Test
    public void interceptorFailUnknownHost(){
        Exception exception = assertThrows(ResourceAccessException.class, () -> client.getConnection().getForEntity(UNKNOWN_HOST_URL, String.class));
        assertThat(exception.getMessage(), is(UNKNOWN_HOST_ERROR_MSG));
    }
}
