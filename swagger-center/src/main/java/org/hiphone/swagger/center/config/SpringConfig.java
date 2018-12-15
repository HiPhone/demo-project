package org.hiphone.swagger.center.config;

import org.hiphone.swagger.center.constants.Constant;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author HiPhone
 */
@Configuration
public class SpringConfig {

    private HttpComponentsClientHttpRequestFactory httpRequestFactory() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(Constant.TIMEOUT_VALUE);
        httpRequestFactory.setReadTimeout(Constant.TIMEOUT_VALUE);
        httpRequestFactory.setConnectionRequestTimeout(Constant.TIMEOUT_VALUE);
        return httpRequestFactory;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(httpRequestFactory());
    }

    @Bean
    @LoadBalanced
    public RestTemplate loadBalanced() {
        return new RestTemplate(httpRequestFactory());
    }
}
