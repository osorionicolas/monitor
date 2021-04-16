package com.fsecure.tools.monitor.unit;


import com.fsecure.tools.monitor.client.HttpClient;
import com.fsecure.tools.monitor.client.ResponseTimeInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpClientTestCase {

    @Test
    public void httpClientOk() {
        HttpClient client = new HttpClient();
        client.init();
        assertThat(client.getConnection().getClass(), is(RestTemplate.class));
        assertThat(client.getConnection().getInterceptors().size(), is(1));
        assertThat(client.getConnection().getInterceptors().get(0).getClass(), is(ResponseTimeInterceptor.class));
    }
}
