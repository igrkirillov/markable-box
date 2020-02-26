package ru.x5.markable.box;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

public class BORestTemplate extends RestTemplate {

    public BORestTemplate() {
        this(1500);
    }

    public BORestTemplate(int timeout) {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(timeout);
        httpRequestFactory.setConnectTimeout(timeout);
        httpRequestFactory.setReadTimeout(timeout);
        this.setRequestFactory(httpRequestFactory);
    }
}
