package com.fsecure.tools.monitor.unit;

import com.fsecure.tools.monitor.client.ResponseTimeInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

import java.io.IOException;
import java.util.List;

import static com.fsecure.tools.monitor.utils.Constants.HEADER_RESPONSE_TIME_IN_MS;
import static java.lang.Long.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

public class ResponseTimeInterceptorTestCase {

    @Test
    public void responseTimeInterceptorOk() throws IOException {
        ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
        HttpRequest request = new MockClientHttpRequest();
        byte[] body = "testingBody".getBytes();
        when(execution.execute(request, body)).thenReturn(new MockClientHttpResponse("OK".getBytes(), OK));
        ResponseTimeInterceptor interceptor = new ResponseTimeInterceptor();
        ClientHttpResponse response = interceptor.intercept(request, body, execution);
        HttpHeaders headers = response.getHeaders();
        assertThat(headers, notNullValue());
        List<String> responseTimeInMsHeader = headers.get(HEADER_RESPONSE_TIME_IN_MS);
        assertThat(responseTimeInMsHeader, notNullValue());
        assertThat(valueOf(responseTimeInMsHeader.get(0)), notNullValue());
    }
}
