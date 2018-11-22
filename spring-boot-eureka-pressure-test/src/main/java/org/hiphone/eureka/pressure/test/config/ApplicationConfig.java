package org.hiphone.eureka.pressure.test.config;

import org.hiphone.eureka.pressure.test.constants.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setReadTimeout(Constant.TIMEOUT_VALUE);
        requestFactory.setConnectTimeout(Constant.TIMEOUT_VALUE);
        requestFactory.setConnectionRequestTimeout(Constant.TIMEOUT_VALUE);
        return new RestTemplate(requestFactory);
    }
}
