package com.fsecure.tools.monitor.functional;

import com.fsecure.tools.monitor.AbstractTestCases;
import com.fsecure.tools.monitor.client.HttpClient;
import com.fsecure.tools.monitor.client.ResponseTimeInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class HttpClientTestCase extends AbstractTestCases {

    @Autowired
    private HttpClient client;

    @Test
    public void httpClientOK(){
        assertThat(client.getConnection().getClass(), is(RestTemplate.class));
        assertThat(client.getConnection().getInterceptors().size(), is(1));
        assertThat(client.getConnection().getInterceptors().get(0).getClass(), is(ResponseTimeInterceptor.class));
    }
}
