package com.fsecure.tools.monitor.client;


import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static com.fsecure.tools.monitor.utils.Constants.HEADER_RESPONSE_TIME_IN_MS;
import static java.lang.System.nanoTime;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class ResponseTimeInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept( HttpRequest request, byte[] body, ClientHttpRequestExecution execution ) throws IOException {
        Long start = nanoTime();
        ClientHttpResponse response = execution.execute( request, body );
        Long end = nanoTime();
        response.getHeaders().set(HEADER_RESPONSE_TIME_IN_MS, Long.toString(MILLISECONDS.convert((end - start), NANOSECONDS)));
        return response;
    }
}
